package com.weizilla.transit.data;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Route
{
    @Element(name = "rt")
    private String id;

    @Element(name = "rtnm")
    private String name;

    public Route()
    {
        /// for simple xml
    }

    public Route(String id)
    {
        this.id = id;
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
    public String toString()
    {
        return "Route{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
