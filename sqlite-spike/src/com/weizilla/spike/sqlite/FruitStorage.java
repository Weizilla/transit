package com.weizilla.spike.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * for storing, retrieving, updating and deleting fruit from it's storage backend
 *
 * @author wei
 *         Date: 11/16/13
 *         Time: 3:16 PM
 */
public class FruitStorage
{
    private FruitStorageDbHelper dbHelper;

    public FruitStorage(Context context)
    {
        dbHelper = new FruitStorageDbHelper(context);
    }

    public long addFruit(Fruit fruit)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FruitContract.FruitEntry.NAME, fruit.getName());
        values.put(FruitContract.FruitEntry.SIZE, fruit.getSize());

        return db.insert(FruitContract.FruitEntry.TABLE_NAME, null, values);
    }

    public Fruit findFruit(String fruitName)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = FruitContract.FruitEntry.NAME + " = ?";
        String[] selectionArgs = {fruitName};
        String[] projection = {FruitContract.FruitEntry.NAME, FruitContract.FruitEntry.SIZE};
        String sortOrder = FruitContract.FruitEntry.NAME + " ASC";

        Cursor cursor = db.query(FruitContract.FruitEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        if (cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            String resultName = cursor.getString(cursor.getColumnIndexOrThrow(FruitContract.FruitEntry.NAME));
            String resultSize = cursor.getString(cursor.getColumnIndexOrThrow(FruitContract.FruitEntry.SIZE));

            return new Fruit(resultName, resultSize);
        }
        else
        {
            return null;
        }

    }

    public int updateFruit(Fruit updatedFruit)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FruitContract.FruitEntry.SIZE, updatedFruit.getSize());

        String selection = FruitContract.FruitEntry.NAME + " = ?";
        String[] selectionArgs = {updatedFruit.getName()};

        int count = db.update(FruitContract.FruitEntry.TABLE_NAME, values, selection, selectionArgs);
        return count;
    }

    public int deleteFruit(String fruitName)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = FruitContract.FruitEntry.NAME + " = ?";
        String[] selectionArgs = {fruitName};

        int numDeleted = db.delete(FruitContract.FruitEntry.TABLE_NAME, selection, selectionArgs);
        return numDeleted;
    }
}
