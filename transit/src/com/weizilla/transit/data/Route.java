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
public class Route
{
    public static final String KEY = Route.class.getName();

    @Element(name = "rt")
    private String id;

    @Element(name = "rtnm")
    private String name;

    public String getId()
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
        return "Route{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
