package com.weizilla.transit.favorites.android;

import android.database.sqlite.SQLiteDatabase;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import com.weizilla.transit.bus.data.Route;
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

import static com.weizilla.transit.favorites.android.db.Favorites.RouteEntry.TABLE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Config(manifest = Config.NONE, emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class AndroidFavoritesStoreRouteTest
{
    private File dbFile;
    private SQLiteDatabase database;
    private IDatabaseTester databaseTester;
    private AndroidFavoritesStore store;

    @Before
    public void setUp() throws Exception
    {
        ShadowLog.stream = System.out;
        dbFile = File.createTempFile("AndroidFavoritesStoreRouteTest-", ".db");
        databaseTester = new JdbcDatabaseTester("org.sqlite.JDBC", "jdbc:sqlite:" + dbFile);
        database = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        FileSqliteDbHelper helper = new FileSqliteDbHelper(dbFile);
        store = new AndroidFavoritesStore(helper);
        executeSql("create_routes_table.sql");
        assertTrue(AndroidDbUtils.tableExists(database, TABLE_NAME));
    }

    @After
    public void tearDown()
    {
        SQLiteDatabase.deleteDatabase(dbFile);
    }

    @Test
    public void getRouteIdsReturnsDbData() throws Exception
    {
        Set<String> expected = Sets.newHashSet("22", "36", "54A");
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(), readDataSet("get_routes.xml"));

        Collection<String> actualIds = store.getFavoriteRouteIds();
        assertEquals(expected, new HashSet<>(actualIds));
    }

    @Test
    public void savesFavoriteRoutes() throws Exception
    {
        Collection<String> routeIds = Lists.newArrayList("134", "156", "J14");
        IDataSet expected = readDataSet("save_routes.xml");
        ITable expectedTable = expected.getTable(TABLE_NAME);

        DatabaseOperation.DELETE_ALL.execute(databaseTester.getConnection(), expected);

        for (String routeId : routeIds)
        {
            store.saveFavorite(new Route(routeId));
        }

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