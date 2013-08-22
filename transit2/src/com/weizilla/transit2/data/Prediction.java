package com.weizilla.transit2.data;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * contains information about a single prediction
 *
 * @author wei
 *         Date: 8/18/13
 *         Time: 5:42 PM
 */
@Root(name="prd")
public class Prediction {

    @Element(name="tmstmp")
    private String timestamp;

    @Element(name="typ")
    private String type;

    @Element(name="stpid")
    private int stopId;

    @Element(name="stpnm")
    private String stopName;

    @Element(name="vid")
    private int vehicleId;

    @Element(name="dstp")
    private int distRemaining;

    @Element(name="rt")
    private String route;

    @Element(name="rtdir")
    private String direction;

    @Element(name="des")
    private String destination;

    @Element(name="prdtm")
    private String predictionTime;

    public String getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public int getStopId() {
        return stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public int getDistRemaining() {
        return distRemaining;
    }

    public String getRoute() {
        return route;
    }

    public String getDirection() {
        return direction;
    }

    public String getDestination() {
        return destination;
    }

    public String getPredictionTime() {
        return predictionTime;
    }

    @Override
    public String toString() {
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
