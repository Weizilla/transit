package com.weizilla.transit.bus.data;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Collections;
import java.util.List;

@Root(name = "bustime-response")
public class BustimeResponse
{

    @ElementList(inline = true, required = false)
    private List<Route> routes;

    @ElementList(inline = true, required = false)
    private List<Direction> directions;

    @ElementList(inline = true, required = false)
    private List<Stop> stops;

    @ElementList(inline = true, required = false)
    private List<Prediction> predictions;

//    @Element(name = "tm", required = false)
//    @Convert(TimeConverter.class)
//    private Date currentTime;

    public List<Prediction> getPredictions()
    {
        return predictions;
    }

    public List<Direction> getDirections()
    {
        return Collections.unmodifiableList(directions);
    }

    public List<Route> getRoutes()
    {
        return Collections.unmodifiableList(routes);
    }

    public List<Stop> getStops()
    {
        return Collections.unmodifiableList(stops);
    }
//
//    public Date getCurrentTime()
//    {
//        return currentTime;
//    }
}
