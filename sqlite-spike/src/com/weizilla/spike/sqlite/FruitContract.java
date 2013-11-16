package com.weizilla.spike.sqlite;

import android.provider.BaseColumns;

/**
 * contract for fruit
 *
 * @author wei
 *         Date: 11/16/13
 *         Time: 2:19 PM
 */
public class FruitContract
{
    private FruitContract() {
        // empty
    }

    public static abstract class FruitEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "fruit";
        public static final String NAME = "name";
        public static final String SIZE = "size";
    }
}
