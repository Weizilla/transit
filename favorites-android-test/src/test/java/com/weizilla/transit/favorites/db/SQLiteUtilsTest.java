package com.weizilla.transit.favorites.db;

import android.database.sqlite.SQLiteDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;

import static org.junit.Assert.*;

@Config(manifest = Config.NONE, emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class SQLiteUtilsTest
{
    private static final String DB_NAME = "SQLiteUtilsTest";
    private File dbFile;
    private SQLiteDatabase database;

    @Before
    public void setUp() throws Exception
    {
        dbFile = File.createTempFile("SQLiteUtilsTest", ".db");
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
        assertFalse(SqliteUtils.tableExists(database, DB_NAME));
    }

    @Test
    public void existingTableReturnsTrue() throws Exception
    {
        database.execSQL("CREATE TABLE " + DB_NAME + " (id INTEGER PRIMARY KEY)");
        assertTrue(SqliteUtils.tableExists(database, DB_NAME));
    }
}
