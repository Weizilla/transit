package com.weizilla.transit.data;

import android.provider.BaseColumns;

import java.util.List;

/**
 * represents a single group of bus stops
 *
 * @author wei
 *         Date: 11/27/13
 *         Time: 5:02 PM
 */
public class Group
{
    private long id = -1; // this is the rows's unique key (_id)
    private String name;
    private List<Integer> stopIds;

    //TODO add stop ids
    public Group(long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stopIds=" + stopIds +
                '}';
    }

    /**
     * holds information on groups
     */
    public static interface DB extends BaseColumns
    {
        public static final String TABLE_NAME = "Groups";
        public static final String NAME = "Name";
    }

    /**
     * links group ids to stop ids. many to many
     */
    public static interface GroupsStopsDB extends BaseColumns
    {
        public static final String TABLE_NAME = "GroupsStops";
        public static final String GROUP_ID = "GroupId";
        public static final String STOP_ID = "StopId";
    }
}
