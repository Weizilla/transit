package com.weizilla.transit.dataproviders;

import com.weizilla.transit.data.Direction;

import java.io.InputStream;
import java.util.List;

/**
 * TODO auto-generated header
 *
 * @author wei
 *         Date: 8/18/13
 *         Time: 5:41 PM
 */
public interface TransitDataProvider
{
    public static final String KEY = TransitDataProvider.class.getName();

    public InputStream getPredictions(List<Integer> stops, List<Integer> routes);

    public InputStream getRoutes();

    public InputStream getStops(String routeId, Direction direction);

    public InputStream getDirections(String route);
}
