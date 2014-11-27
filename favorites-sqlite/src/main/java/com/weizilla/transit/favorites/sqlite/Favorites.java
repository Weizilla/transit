package com.weizilla.transit.favorites.sqlite;

public class Favorites
{
    private Favorites() {
        // private
    }

    public abstract static class RouteEntry
    {
        public static final String TABLE_NAME = "fav_routes";
        public static final String ID = "id";
    }

    public abstract static class StopEntry
    {
        public static final String TABLE_NAME = "fav_stops";
        public static final String ID = "id";
    }
}
