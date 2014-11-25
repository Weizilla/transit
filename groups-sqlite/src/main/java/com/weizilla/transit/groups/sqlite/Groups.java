package com.weizilla.transit.groups.sqlite;

public class Groups
{
    private Groups()
    {
        // private
    }

    public abstract static class GroupEntry
    {
        public static final String TABLE_NAME = "groups";
        public static final String _ID = "_id";
        public static final String NAME = "name";
    }

    public abstract static class StopEntry
    {
        public static final String TABLE_NAME = "stops";
        public static final String GROUP_ID = "group_id";
        public static final String STOP_ID = "stop_id";
        public static final String STOP_NAME = "stop_name";
        public static final String ROUTE_ID = "route_id";
        public static final String DIRECTION = "direction";
    }
}
