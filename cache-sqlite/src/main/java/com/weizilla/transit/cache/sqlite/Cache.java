package com.weizilla.transit.cache.sqlite;

public class Cache
{
    private Cache() {
        // private
    }

    public abstract static class RouteEntry
    {
        public static final String TABLE_NAME = "cache_routes";
        public static final String ID = "id";
        public static final String NAME = "name";
    }

    public abstract static class StopEntry
    {
        public static final String TABLE_NAME = "cache_stops";
        public static final String ID = "id";
        public static final String NAME = "name";
    }
}
