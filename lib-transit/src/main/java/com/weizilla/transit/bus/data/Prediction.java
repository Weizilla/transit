package com.weizilla.transit.bus.data;

import com.weizilla.transit.utils.TimeConverter;
import org.joda.time.DateTime;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

@Root(name = "prd", strict = false)
public class Prediction
{
    @Element(name = "tmstmp")
    @Convert(TimeConverter.class)
    private DateTime generated;

    @Element(name = "typ")
    private String type;

    @Element(name = "stpid")
    private int stopId;

    @Element(name = "stpnm")
    private String stopName;

    @Element(name = "vid")
    private int vehicleId;

    @Element(name = "dstp")
    private int distanceFt;

    @Element(name = "rt")
    private String route;

    @Element(name = "rtdir")
    private Direction routeDirection;

    @Element(name = "des")
    private String destination;

    @Element(name = "prdtm")
    @Convert(TimeConverter.class)
    private DateTime prediction;

    @Element(name = "dly", required = false)
    private boolean delayed;

    public DateTime getGenerated()
    {
        return generated;
    }

    public int getStopId()
    {
        return stopId;
    }

    public String getStopName()
    {
        return stopName;
    }

    public int getDistanceFt()
    {
        return distanceFt;
    }

    public String getRoute()
    {
        return route;
    }

    public Direction getRouteDirection()
    {
        return routeDirection;
    }

    public String getDestination()
    {
        return destination;
    }

    public DateTime getPrediction()
    {
        return prediction;
    }

    public boolean isDelayed()
    {
        return delayed;
    }
}
