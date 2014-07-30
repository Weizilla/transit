package com.weizilla.transit.bus.source;

import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Prediction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;

import java.util.Collection;

public class BusDataSourceStub implements BusDataSource
{
    private Collection<Route> routes;
    private String routeKey;
    private Direction directionKey;
    private int stopIdKey;
    private Collection<Direction> directions;
    private Collection<Stop> stops;
    private Collection<Prediction> predictions;

    public BusDataSourceStub(Collection<Route> routes)
    {
        this.routes = routes;
    }

    public BusDataSourceStub(String route, Collection<Direction> directions)
    {
        routeKey = route;
        this.directions = directions;
    }

    public BusDataSourceStub(String route, Direction direction, Collection<Stop> stops)
    {
        routeKey = route;
        directionKey = direction;
        this.stops = stops;
    }

    public BusDataSourceStub(String route, int stopId, Collection<Prediction> predictions)
    {
        routeKey = route;
        stopIdKey = stopId;
        this.predictions = predictions;
    }

    @Override
    public Collection<Route> getRoutes()
    {
        return routes;
    }

    @Override
    public Collection<Direction> getDirections(String route)
    {
        return route.equals(routeKey) ? directions : null;
    }

    @Override
    public Collection<Stop> getStops(String route, Direction direction)
    {
        return route.equals(routeKey) && direction == directionKey ? stops : null;
    }

    @Override
    public Collection<Prediction> getPredictions(String route, int stopId)
    {
        return route.equals(routeKey) && stopId == stopIdKey ? predictions : null;
    }
}
