package com.weizilla.transit.dataproviders;

import com.weizilla.transit.data.Direction;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

/**
 * interface for anything that provides streams of transit data
 *
 * @author wei
 *         Date: 8/18/13
 *         Time: 5:41 PM
 */
public interface TransitDataProvider extends Serializable
{
    public static final String KEY = "com.weizilla.transit.dataproviders.TransitDataProvider";

    public InputStream getPredictions(List<Integer> stops, List<Integer> routes);

    public InputStream getRoutes();

    public InputStream getStops(String routeId, Direction direction);

    public InputStream getDirections(String route);

    public InputStream getCurrentTime();
}
