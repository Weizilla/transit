package com.weizilla.transit.favorites.android;

import android.database.sqlite.SQLiteDatabase;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Stop;
import com.weizilla.transit.favorites.android.db.AndroidDbUtils;
import com.weizilla.transit.favorites.android.db.FileSqliteDbHelper;
import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.weizilla.transit.favorites.android.db.Favorites.StopEntry.TABLE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Config(manifest = Config.NONE, emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class AndroidFavoritesStoreStopTest
{
    private File dbFile;
    private SQLiteDatabase database;
    private IDatabaseTester databaseTester;
    private AndroidFavoritesStore store;

    @Before
    public void setUp() throws Exception
    {
        ShadowLog.stream = System.out;
        dbFile = File.createTempFile("AndroidFavoritesStoreStopTest-", ".db");
        databaseTester = new JdbcDatabaseTester("org.sqlite.JDBC", "jdbc:sqlite:" + dbFile);
        database = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        FileSqliteDbHelper helper = new FileSqliteDbHelper(dbFile);
        store = new AndroidFavoritesStore(helper);
        executeSql("create_stops_table.sql");
        assertTrue(AndroidDbUtils.tableExists(database, TABLE_NAME));
    }

    @After
    public void tearDown()
    {
        SQLiteDatabase.deleteDatabase(dbFile);
    }

    @Test
    public void getStopIdsReturnsDbData() throws Exception
    {
        String route = "22";
        Direction direction = Direction.Northbound;
        Set<Integer> expected = Sets.newHashSet(100, 400);
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(),
            readDataSet("get_stops.xml"));

        Collection<Integer> actualIds = store.getFavoriteStopIds(route, direction);
        assertEquals(expected, new HashSet<>(actualIds));
    }

    @Test
    public void savesFavoriteStops() throws Exception
    {
        IDataSet expected = readDataSet("save_stops.xml");
        ITable expectedTable = expected.getTable(TABLE_NAME);

        DatabaseOperation.DELETE_ALL.execute(databaseTester.getConnection(), expected);

        store.saveFavorite(new Stop(100, "36", Direction.Northbound));
        store.saveFavorite(new Stop(200, "N22", Direction.Westbound));
        store.saveFavorite(new Stop(300, "156", Direction.Eastbound));
        store.saveFavorite(new Stop(400, "N22", Direction.Westbound));

        ITable actual = getTable(TABLE_NAME);
        ITable actualFiltered = DefaultColumnFilter.includedColumnsTable(actual, expectedTable.getTableMetaData().getColumns());

        Assertion.assertEquals(expectedTable, actualFiltered);
    }

    //TODO move to common utils
    protected static IDataSet readDataSet(String filename) throws Exception
    {
        URL file = Resources.getResource(filename);
        return new FlatXmlDataSetBuilder().build(file);
    }

    protected ITable getTable(String tableName) throws Exception
    {
        return databaseTester.getConnection().createDataSet().getTable(tableName);
    }

    private void executeSql(String filename) throws Exception
    {
        //TODO move into utils
        URL url = Resources.getResource(filename);
        String sql = Resources.toString(url, Charsets.UTF_8);
        database.execSQL(sql);
    }
}