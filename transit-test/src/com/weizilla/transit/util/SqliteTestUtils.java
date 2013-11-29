package com.weizilla.transit.util;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import com.weizilla.transit.db.SqliteUtils;

/**
 *
 * Sqlite utilities for testing
 *
 * @author wei
 *         Date: 11/28/13
 *         Time: 9:43 PM
 */
public class SqliteTestUtils
{
    public static void assertRowCount(SQLiteDatabase db, String table, int count)
    {
        int actualCount = SqliteUtils.countRows(db, table);
        AndroidTestCase.assertEquals(count, actualCount);
    }
}
