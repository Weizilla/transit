package com.weizilla.transit.data;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.Collections;
import java.util.List;

@Root(name = "bustime-response", strict = false)
public class BusResponse
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

    @Element(name = "msg", required = false)
    @Path("error")
    private String errorMsg;

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

    public String getErrorMsg()
    {
        return errorMsg;
    }

    //
//    public Date getCurrentTime()
//    {
//        return currentTime;
//    }
}
