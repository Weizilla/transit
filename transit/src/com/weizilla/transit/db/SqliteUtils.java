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
    private static final String COUNT_ROWS_SQL = "SELECT count(*) FROM ";
    private SqliteUtils()
    {
        // empty
    }

    public static String print(Cursor cursor)
    {
        StringBuilder builder = new StringBuilder();

        cursor.moveToFirst();

        for (int i = 0; i < cursor.getColumnCount(); i++)
        {
            String colName = cursor.getColumnName(i);
            builder.append(colName).append("|");
        }

        builder.append("\n");

        do
        {
            for (int i = 0; i < cursor.getColumnCount(); i++)
            {
                String value = cursor.getString(i);
                builder.append(value).append("|");
            }
            builder.append("\n");
        }
        while (cursor.moveToNext());

        return builder.toString();
    }

    public static int countTables(SQLiteDatabase db, String tableName)
    {
        Cursor cursor = db.rawQuery(COUNT_TABLES_SQL, new String[]{tableName});
        try
        {
            return getSingleValue(cursor, 0);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    public static int countRows(SQLiteDatabase db, String tableName)
    {
        String sql = COUNT_ROWS_SQL + tableName;
        Cursor cursor = db.rawQuery(sql, null);
        try
        {
            return getSingleValue(cursor, 0);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    public static int getSingleValue(Cursor cursor, int defaultValue)
    {
        if (cursor == null || cursor.getCount() == 0)
        {
            return defaultValue;
        }
        else
        {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
    }

}
