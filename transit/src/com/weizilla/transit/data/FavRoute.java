package com.weizilla.transit.data;

import android.provider.BaseColumns;

/**
 * represents the db object for storing a favorite route
 *
 * @author wei
 *         Date: 11/21/13
 *         Time: 9:16 PM
 */
public class FavRoute
{
    public static interface DB extends BaseColumns
    {
        public static final String TABLE_NAME = "Route";
        public static final String ID = "RouteId";
        public static final String NAME = "Name";
    }
}
