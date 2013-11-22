package com.weizilla.transit.data;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * TODO auto-generated header
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
    public int id;

    @Element(name = "stpnm")
    public String name;

    @Element(name = "lat")
    public double latitude;

    @Element(name = "lon")
    public double longitude;

    public Stop()
    {
        // default
    }

    public Stop(Parcel parcel)
    {
        this.id = parcel.readInt();
        this.name = parcel.readString();
        this.latitude = parcel.readDouble();
        this.longitude = parcel.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
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

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    @Override
    public String toString()
    {
        return "Stop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
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
