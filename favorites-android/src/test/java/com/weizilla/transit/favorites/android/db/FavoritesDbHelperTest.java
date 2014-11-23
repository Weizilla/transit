package com.weizilla.transit.favorites.android.db;

import android.database.sqlite.SQLiteDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(manifest = Config.NONE, emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class FavoritesDbHelperTest
{
    private static final String TABLE_NAME = "fav_routes"; //TODO put in single place
    private FavoritesDbHelper helper;
    private File dbFile;
    private SQLiteDatabase database;

    @Before
    public void setUp() throws Exception
    {
        dbFile = File.createTempFile("FavoritesDbHelperTest-", ".db");
        database = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        helper = new FavoritesDbHelper(null);
    }

    @After
    public void tearDown()
    {
        SQLiteDatabase.deleteDatabase(dbFile);
    }

    //TODO test constructor

    @Test
    public void createsTablesOnCreate() throws Exception
    {
        assertFalse(AndroidDbUtils.tableExists(database, TABLE_NAME));
        helper.onCreate(database);
        assertTrue(AndroidDbUtils.tableExists(database, TABLE_NAME));
    }
}