package com.weizilla.transit.favorites.android.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.io.File;

@Config(manifest = Config.NONE, emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public abstract class AndroidSqliteTest
{
    protected File dbFile;
    protected SQLiteDatabase database;
    private String TAG = getClass().getSimpleName();

    @Before
    public void setUp() throws Exception
    {
        ShadowLog.stream = System.out;
        dbFile = File.createTempFile(TAG + '-', ".db");
        Log.d(TAG, "Temp test db file: " + dbFile.getAbsolutePath());
        database = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    @After
    public void tearDown()
    {
        SQLiteDatabase.deleteDatabase(dbFile);
    }
}
