package com.weizilla.transit.favorites.android.db;

import android.provider.BaseColumns;

public class Favorites
{
    private Favorites() {
        // private
    }

    public static abstract class RouteEntry implements BaseColumns {
        public static final String TABLE_NAME = "fav_routes";
        public static final String ID = "id";
        public static final String NAME = "name";
    }

    public static abstract class StopEntry implements BaseColumns {
        public static final String TABLE_NAME = "fav_stops";
        public static final String ID = "id";
        public static final String ROUTE = "route";
        public static final String DIRECTION = "direction";
    }
}
