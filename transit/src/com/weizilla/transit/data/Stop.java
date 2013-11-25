package com.weizilla.transit.data;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * represents a bus stop
 *
 * @author wei
 *         Date: 9/4/13
 *         Time: 5:39 AM
 */
@Root(name = "stop")
public class Stop implements Parcelable
{
    public static final String KEY = Stop.class.getName();

    @Element(name = "stpid")
    private int id;

    @Element(name = "stpnm")
    private String name;

    // not used
    @Element(name = "lat")
    private double latitude;

    // not used
    @Element(name = "lon")
    private double longitude;

    private boolean isFavorite;

    public Stop()
    {
        // default
    }

    public Stop(int id, String name, boolean isFavorite)
    {
        this.id = id;
        this.name = name;
        this.isFavorite = isFavorite;
    }

    public Stop(Parcel parcel)
    {
        this.id = parcel.readInt();
        this.name = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public boolean isFavorite()
    {
        return isFavorite;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Stop stop = (Stop) o;

        if (id != stop.id)
        {
            return false;
        }
        if (isFavorite != stop.isFavorite)
        {
            return false;
        }
        if (name != null ? !name.equals(stop.name) : stop.name != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (isFavorite ? 1 : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Stop{" +
                "isFavorite=" + isFavorite +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public static final Creator<Stop> CREATOR = new Creator<Stop>()
    {
        @Override
        public Stop createFromParcel(Parcel source)
        {
            return new Stop(source);
        }

        @Override
        public Stop[] newArray(int size)
        {
            return new Stop[size];
        }
    };
}
