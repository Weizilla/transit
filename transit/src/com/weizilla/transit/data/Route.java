package com.weizilla.transit.data;

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
public class Route implements Comparable<Route>
{
    public static final String KEY = Route.class.getName();

    @Element(name = "rt")
    private String id;

    @Element(name = "rtnm")
    private String name;

    public Route()
    {
        // default for simple-xml
    }

    public Route(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
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
        return result;
    }

    @Override

    public String toString()
    {
        return "Route{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(Route another)
    {
        int ret = compareString(id, another.id);
        if (ret == 0)
        {
            ret = compareString(name, another.name);
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

}
