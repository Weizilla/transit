package com.weizilla.transit.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * holds a list of stops for sending as parcel
 *
 * @author wei
 *         Date: 12/1/13
 *         Time: 2:04 PM
 */
public class StopList implements Parcelable
{
    public static final String INTENT_KEY = "com.weizilla.transit.data.StopList";
    private List<Stop> stops = new ArrayList<>();

    public StopList(Stop stop)
    {
        stops.add(stop);
    }

    public StopList(List<Stop> stops)
    {
        this.stops = stops;
    }

    public StopList(Parcel parcel)
    {
        parcel.readTypedList(stops, Stop.CREATOR);
    }

    public List<Stop> getStops()
    {
        return stops;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeTypedList(stops);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<StopList> CREATOR = new Creator<StopList>()
    {
        @Override
        public StopList createFromParcel(Parcel source)
        {
            return new StopList(source);
        }

        @Override
        public StopList[] newArray(int size)
        {
            return new StopList[size];
        }
    };
}
