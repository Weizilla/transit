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

    private Direction direction;
    private String routeId;

    public Stop()
    {
        // for simple xml
    }

    public Stop(int id)
    {
        this.id = id;
    }

    public Stop(int id, String routeId, Direction direction)
    {
        this.id = id;
        this.routeId = routeId;
        this.direction = direction;
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

    public Direction getDirection()
    {
        return direction;
    }

    public void setDirection(Direction direction)
    {
        this.direction = direction;
    }

    public String getRouteId()
    {
        return routeId;
    }

    public void setRouteId(String routeId)
    {
        this.routeId = routeId;
    }
}
