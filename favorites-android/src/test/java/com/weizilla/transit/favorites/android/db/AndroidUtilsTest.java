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
public class AndroidUtilsTest
{
    private static final String DB_NAME = "AndroidUtilsTest";
    private File dbFile;
    private SQLiteDatabase database;

    @Before
    public void setUp() throws Exception
    {
        dbFile = File.createTempFile("AndroidUtilsTest-", ".db");
        database = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    @After
    public void tearDown()
    {
        SQLiteDatabase.deleteDatabase(dbFile);
    }

    @Test
    public void noTableReturnsFalse() throws Exception
    {
        database.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        assertFalse(AndroidDbUtils.tableExists(database, DB_NAME));
    }

    @Test
    public void existingTableReturnsTrue() throws Exception
    {
        assertFalse(AndroidDbUtils.tableExists(database, DB_NAME));
        database.execSQL("CREATE TABLE " + DB_NAME + " (id INTEGER PRIMARY KEY)");
        assertTrue(AndroidDbUtils.tableExists(database, DB_NAME));
    }
}
