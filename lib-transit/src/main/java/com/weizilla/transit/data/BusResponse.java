package com.weizilla.transit.data;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.unmodifiableList;

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

    @ElementList(inline = true, required = false)
    private List<Error> errors;
//    @Element(name = "tm", required = false)
//    @Convert(TimeConverter.class)
//    private Date currentTime;


    public List<Prediction> getPredictions()
    {
        return unmodifiable(predictions);
    }

    public List<Direction> getDirections()
    {
        return unmodifiable(directions);
    }

    public List<Route> getRoutes()
    {
        return unmodifiable(routes);
    }

    public List<Stop> getStops()
    {
        return unmodifiable(stops);
    }

    public List<Error> getErrors()
    {
        return unmodifiable(errors);
    }

    private static <T> List<T> unmodifiable(List<T> data)
    {
        return data == null || data.isEmpty() ? Collections.<T>emptyList() : unmodifiableList(data);
    }

    //
//    public Date getCurrentTime()
//    {
//        return currentTime;
//    }
}
