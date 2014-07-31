package com.weizilla.transit.bus.data;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Stop
{
    @Element(name = "stpid")
    private int id;

    @Element(name = "stpnm")
    private String name;

    @Element(name = "lat")
    private double latitude;

    @Element(name = "lon")
    private double longitude;

    public Stop()
    {
        // for simple xml
    }

    public Stop(int id)
    {
        this.id = id;
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
}
