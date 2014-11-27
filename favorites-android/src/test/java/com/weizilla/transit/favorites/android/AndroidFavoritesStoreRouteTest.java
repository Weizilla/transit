package com.weizilla.transit.favorites.android;

import android.database.sqlite.SQLiteDatabase;
import com.weizilla.transit.favorites.android.db.AndroidDbUtils;
import com.weizilla.transit.favorites.android.db.FileSqliteDbHelper;
import com.weizilla.transit.favorites.sqlite.BaseSqliteFavStoreRouteTest;
import com.weizilla.transit.utils.ResourceUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import static com.weizilla.transit.favorites.sqlite.Favorites.RouteEntry.TABLE_NAME;
import static org.junit.Assert.assertTrue;

@Config(manifest = Config.NONE, emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class AndroidFavoritesStoreRouteTest extends BaseSqliteFavStoreRouteTest
{
    private SQLiteDatabase database;

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        ShadowLog.stream = System.out;
        database = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        store = new AndroidFavoritesStore(new FileSqliteDbHelper(dbFile));
        executeSqlFromFile("create_fav_routes_table.sql");
        assertTrue(AndroidDbUtils.tableExists(database, TABLE_NAME));
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        super.tearDown();
        SQLiteDatabase.deleteDatabase(dbFile);
    }

    @Override
    protected void executeSqlFromFile(String filename) throws Exception
    {
        String sql = ResourceUtils.readFile(filename);
        database.execSQL(sql);
    }
}