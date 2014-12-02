package com.weizilla.transit.android.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AndroidDbUtils
{
    private static final String COUNT_TABLES_SQL =
            "SELECT count(name) FROM sqlite_master WHERE type='table' AND name=?";

    private AndroidDbUtils()
    {
        // util class
    }

    public static boolean tableExists(SQLiteDatabase db, String tableName)
    {
        try
        (
            Cursor cursor = db.rawQuery(COUNT_TABLES_SQL, new String[]{tableName})
        )
        {
            cursor.moveToFirst();
            int numTables = cursor.getInt(0);
            return numTables > 0;
        }
    }
}
