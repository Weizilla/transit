package com.weizilla.transit.data;

import com.weizilla.transit.util.TimeConverter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import java.util.Date;
import java.util.List;

/**
 * holds a list of bus tracker elements
 *
 * @author wei
 *         Date: 8/22/13
 *         Time: 5:47 AM
 */
@Root(name = "bustime-response")
public class BustimeResponse
{
    @ElementList(inline = true, required = false)
    private List<Prediction> predictions;

    @ElementList(inline = true, required = false)
    private List<Route> routes;

    @ElementList(inline = true, required = false)
    private List<Direction> directions;

    @ElementList(inline = true, required = false)
    private List<Stop> stops;

    @Element(name = "tm", required = false)
    @Convert(TimeConverter.class)
    private Date currentTime;

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

    public List<Stop> getStops()
    {
        return stops;
    }

    public Date getCurrentTime()
    {
        return currentTime;
    }
}
