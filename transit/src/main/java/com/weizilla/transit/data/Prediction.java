package com.weizilla.transit.data;

import com.weizilla.transit.util.TimeConverter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import java.util.Date;

/**
 * contains information about a single prediction
 *
 * @author wei
 *         Date: 8/18/13
 *         Time: 5:42 PM
 */
@Root(name = "prd")
public class Prediction implements Comparable<Prediction>
{
    @Element(name = "tmstmp")
    @Convert(TimeConverter.class)
    private Date timestamp;

    @Element(name = "typ")
    private String type;

    @Element(name = "stpid")
    private int stopId;

    @Element(name = "stpnm")
    private String stopName;

    @Element(name = "vid")
    private int vehicleId;

    @Element(name = "dstp")
    private int distRemaining;

    @Element(name = "rt")
    private String route;

    @Element(name = "rtdir")
    private String direction;

    @Element(name = "des")
    private String destination;

    @Element(name = "prdtm")
    @Convert(TimeConverter.class)
    private Date predictionTime;

    @Element(name = "dly", required = false)
    private boolean delayed;

    @Override
    public int compareTo(Prediction another)
    {
        //TODO what about delays?
        return predictionTime.compareTo(another.predictionTime);
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public String getType()
    {
        return type;
    }

    public int getStopId()
    {
        return stopId;
    }

    public String getStopName()
    {
        return stopName;
    }

    public int getVehicleId()
    {
        return vehicleId;
    }

    public int getDistRemaining()
    {
        return distRemaining;
    }

    public String getRoute()
    {
        return route;
    }

    public String getDirection()
    {
        return direction;
    }

    public String getDestination()
    {
        return destination;
    }

    public Date getPredictionTime()
    {
        return predictionTime;
    }

    @Override
    public String toString()
    {
        return "Prediction{" +
                "timestamp='" + timestamp + '\'' +
                ", type='" + type + '\'' +
                ", stopId=" + stopId +
                ", stopName='" + stopName + '\'' +
                ", vehicleId=" + vehicleId +
                ", distRemaining=" + distRemaining +
                ", route='" + route + '\'' +
                ", direction='" + direction + '\'' +
                ", destination='" + destination + '\'' +
                ", predictionTime='" + predictionTime + '\'' +
                '}';
    }
}
