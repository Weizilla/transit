package com.weizilla.transit.data;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * holds a bus route
 *
 * @author wei
 *         Date: 8/22/13
 *         Time: 6:34 AM
 */
@Root
public class Route implements Parcelable, Comparable<Route>
{
    public static final String KEY = "com.weizilla.transit.data.Route";

    @Element(name = "rt")
    private String id;

    @Element(name = "rtnm")
    private String name;

    private boolean isFavorite = false;

    public Route()
    {
        // default for simple-xml
    }

    public Route(String id, String name, boolean isFavorite)
    {
        this.id = id;
        this.name = name;
        this.isFavorite = isFavorite;
    }

    public Route(Parcel parcel)
    {
        id = parcel.readString();
        name = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(name);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public String getId()
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

        Route route = (Route) o;

        if (isFavorite != route.isFavorite)
        {
            return false;
        }
        if (id != null ? !id.equals(route.id) : route.id != null)
        {
            return false;
        }
        if (name != null ? !name.equals(route.name) : route.name != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (isFavorite ? 1 : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Route{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isFavorite=" + isFavorite +
                '}';
    }

    @Override
    public int compareTo(Route another)
    {
        // sort by id descending, then name decending, then favorite first
        // nulls are sorted above values
        int ret = compareString(id, another.id);
        if (ret == 0)
        {
            ret = compareString(name, another.name);
        }
        if (ret == 0)
        {
            ret = compareFavorite(isFavorite, another.isFavorite);
        }

        return ret;
    }

    private static int compareString(String lhs, String rhs)
    {
        if (lhs == null && rhs == null)
        {
            return 0;
        }
        else if (lhs == null)
        {
            return -1;
        }
        else if (rhs == null)
        {
            return 1;
        }
        else
        {
            return lhs.compareTo(rhs);
        }
    }

    private int compareFavorite(boolean lhs, boolean rhs)
    {
        // return 0 if same
        // if not, sort the one with 'true' in front
        if (lhs == rhs)
        {
            return 0;
        }
        else if (lhs)
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }

    public static final Creator<Route> CREATOR = new Creator<Route>()
    {
        @Override
        public Route createFromParcel(Parcel source)
        {
            return new Route(source);
        }

        @Override
        public Route[] newArray(int size)
        {
            return new Route[size];
        }
    };
}
