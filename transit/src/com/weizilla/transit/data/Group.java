package com.weizilla.transit.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import com.google.common.primitives.Longs;
import com.weizilla.transit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * represents a single group of bus stops
 *
 * @author wei
 *         Date: 11/27/13
 *         Time: 5:02 PM
 */
public class Group implements Parcelable, Comparable<Group>
{
    public static final String INTENT_KEY = "com.weizilla.transit.data.Group";
    private long id = -1; // this is the rows's unique key (_id)
    private String name;
    private List<Stop> stops = new ArrayList<>();

    public Group(long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Group(Parcel parcel)
    {
        id = parcel.readLong();
        name = parcel.readString();
        parcel.readTypedList(stops, Stop.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeTypedList(stops);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public List<Stop> getStops()
    {
        return stops;
    }

    public void addStop(Stop stop)
    {
        stops.add(stop);
    }

    @Override
    public String toString()
    {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stops=" + stops +
                '}';
    }

    @Override
    public int compareTo(Group another)
    {
        int ret = Longs.compare(id, another.id);
        if (ret == 0)
        {
            ret = StringUtil.compare(name, another.name);
        }
        return ret;
    }

    public static final Creator<Group> CREATOR = new Creator<Group>()
    {
        @Override
        public Group createFromParcel(Parcel source)
        {
            return new Group(source);
        }

        @Override
        public Group[] newArray(int size)
        {
            return new Group[size];
        }
    };

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
        public static final String STOP_NAME = "StopName";
    }
}
