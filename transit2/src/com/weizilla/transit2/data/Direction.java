package com.weizilla.transit2.data;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.Root;

/**
 * enumeration of direction
 *
 * @author wei
 *         Date: 9/3/13
 *         Time: 9:20 PM
 */
@Root(name = "dir")
public enum Direction implements Parcelable
{
    Northbound,
    Eastbound,
    Southbound,
    Westbound;

    public static final String KEY = Direction.class.getName();

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static Direction readFromParcel(Parcel source)
    {
        int ordinal = source.readInt();
        return Direction.values()[ordinal];
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(ordinal());
    }

    public static final Creator<Direction> CREATOR = new Creator<Direction>()
    {
        @Override
        public Direction createFromParcel(Parcel source)
        {
            return Direction.readFromParcel(source);
        }

        @Override
        public Direction[] newArray(int size)
        {
            return new Direction[size];
        }
    };

}
