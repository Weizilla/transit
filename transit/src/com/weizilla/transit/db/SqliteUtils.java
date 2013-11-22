package com.weizilla.transit.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * utility fuctions for sql lite
 *
 * @author wei
 *         Date: 11/22/13
 *         Time: 11:26 AM
 */
public class SqliteUtils
{
    private static final String COUNT_TABLES_SQL = "SELECT count(name) FROM sqlite_master WHERE type='table' AND name=?";
    private SqliteUtils()
    {
        // empty
    }

    public static int countTables(SQLiteDatabase db, String tableName)
    {
        Cursor cursor = db.rawQuery(COUNT_TABLES_SQL, new String[]{tableName});
        if (cursor == null || cursor.getCount() == 0)
        {
            return 0;
        }
        else
        {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
    }

}
