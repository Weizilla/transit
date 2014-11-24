package com.weizilla.transit.favorites.sqlite;

public class Favorites
{
    private Favorites() {
        // private
    }

    public abstract static class RoutesEntry {
        public static final String TABLE_NAME = "fav_routes";
        public static final String ID = "id";
        public static final String NAME = "name";
    }

    public abstract static class StopsEntry {
        public static final String TABLE_NAME = "fav_stops";
        public static final String ID = "id";
        public static final String ROUTE = "route";
        public static final String DIRECTION = "direction";
    }
}
