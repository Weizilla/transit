package com.weizilla.transit.bus.source;

import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Prediction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;

import java.util.Collection;

public class BusDataSourceStub implements BusDataSource
{
    private Collection<Route> routes;
    private Route routeKey;
    private Direction directionKey;
    private Stop stopKey;
    private Collection<Direction> directions;
    private Collection<Stop> stops;
    private Collection<Prediction> predictions;

    public BusDataSourceStub(Collection<Route> routes)
    {
        this.routes = routes;
    }

    public BusDataSourceStub(Route route, Collection<Direction> directions)
    {
        routeKey = route;
        this.directions = directions;
    }

    public BusDataSourceStub(Route route, Direction direction, Collection<Stop> stops)
    {
        routeKey = route;
        directionKey = direction;
        this.stops = stops;
    }

    public BusDataSourceStub(Route route, Stop stop, Collection<Prediction> predictions)
    {
        routeKey = route;
        stopKey = stop;
        this.predictions = predictions;
    }

    @Override
    public Collection<Route> getRoutes()
    {
        return routes;
    }

    @Override
    public Collection<Direction> getDirections(Route route)
    {
        return route.equals(routeKey) ? directions : null;
    }

    @Override
    public Collection<Stop> getStops(Route route, Direction direction)
    {
        return route.equals(routeKey) && direction == directionKey ? stops : null;
    }

    @Override
    public Collection<Prediction> getPredictions(Route route, Stop stop)
    {
        return route.equals(routeKey) && stop.equals(stopKey) ? predictions : null;
    }
}
