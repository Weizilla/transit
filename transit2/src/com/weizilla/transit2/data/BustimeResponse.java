package com.weizilla.transit2.data;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * holds a list of bus tracker elements
 * @author wei
 * Date: 8/22/13
 * Time: 5:47 AM
 */
@Root(name="bustime-response")
public class BustimeResponse {

    @ElementList(inline = true, required = false)
    private List<Prediction> predictions;

    @ElementList(inline = true, required = false)
    private List<Route> routes;

    @ElementList(inline = true, required = false)
    private List<Direction> directions;

    public List<Prediction> getPredictions()
    {
        return predictions;
    }

    public List<Direction> getDirections()
    {
        return directions;
    }

    public List<Route> getRoutes()
    {
        return routes;
    }
}
