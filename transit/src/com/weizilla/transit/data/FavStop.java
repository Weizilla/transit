package com.weizilla.transit.data;

import android.provider.BaseColumns;

/**
 * represents the db object for storing a favorite bus stop
 *
 * @author wei
 *         Date: 11/24/13
 *         Time: 6:23 PM
 */
public class FavStop
{
    public static interface DB extends BaseColumns
    {
        public static final String TABLE_NAME = "Stop";
        public static final String ID = "Id";
        public static final String NAME = "Name";
        public static final String ROUTE_ID = "RouteId";
        public static final String ROUTE_DIR = "RouteDir";
    }
}
