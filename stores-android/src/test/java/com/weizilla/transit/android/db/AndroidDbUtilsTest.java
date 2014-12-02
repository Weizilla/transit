package com.weizilla.transit.android.db;

import org.junit.Test;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(manifest = Config.NONE)
public class AndroidDbUtilsTest extends AndroidSqliteTest
{
    private static final String DB_NAME = "AndroidUtilsTest";

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
