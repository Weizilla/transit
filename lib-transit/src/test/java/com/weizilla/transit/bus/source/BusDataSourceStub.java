package com.weizilla.transit.bus.source;

import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Route;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BusDataSourceStub implements BusDataSource
{
    private Collection<Route> routes;
    private Map<String, Collection<Direction>> directions = new HashMap<>();

    public BusDataSourceStub(Collection<Route> routes)
    {
        this.routes = routes;
    }

    public BusDataSourceStub(String route, Collection<Direction> directions)
    {
        this.directions.put(route, directions);
    }

    @Override
    public Collection<Route> getRoutes()
    {
        return routes;
    }

    @Override
    public Collection<Direction> getDirections(String route)
    {
        return directions.get(route);
    }
}
