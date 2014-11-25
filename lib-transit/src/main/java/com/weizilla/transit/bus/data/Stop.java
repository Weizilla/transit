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

    public Stop(int id, String name)
    {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString()
    {
        return "Stop{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            ", direction=" + direction +
            ", routeId='" + routeId + '\'' +
            '}';
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
        if (Double.compare(stop.latitude, latitude) != 0)
        {
            return false;
        }
        if (Double.compare(stop.longitude, longitude) != 0)
        {
            return false;
        }
        if (direction != stop.direction)
        {
            return false;
        }
        if (name != null ? !name.equals(stop.name) : stop.name != null)
        {
            return false;
        }
        if (routeId != null ? !routeId.equals(stop.routeId) : stop.routeId != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result;
        long temp;
        result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        result = 31 * result + (routeId != null ? routeId.hashCode() : 0);
        return result;
    }
}
