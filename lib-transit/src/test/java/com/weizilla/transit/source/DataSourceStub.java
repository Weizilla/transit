package com.weizilla.transit.source;

import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Prediction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;

import java.util.Collection;

public class DataSourceStub implements DataSource
{
    private Collection<Route> routes;
    private String routeKey;
    private Direction directionKey;
    private int stopKey;
    private Collection<Direction> directions;
    private Collection<Stop> stops;
    private Collection<Prediction> predictions;

    public DataSourceStub(Collection<Route> routes)
    {
        this.routes = routes;
    }

    public DataSourceStub(String routeId, Collection<Direction> directions)
    {
        routeKey = routeId;
        this.directions = directions;
    }

    public DataSourceStub(String routeId, Direction direction, Collection<Stop> stops)
    {
        routeKey = routeId;
        directionKey = direction;
        this.stops = stops;
    }

    public DataSourceStub(String routeId, int stopId, Collection<Prediction> predictions)
    {
        routeKey = routeId;
        stopKey = stopId;
        this.predictions = predictions;
    }

    @Override
    public Collection<Route> getRoutes()
    {
        return routes;
    }

    @Override
    public Collection<Direction> getDirections(String routeId)
    {
        return routeId.equals(routeKey) ? directions : null;
    }

    @Override
    public Collection<Stop> getStops(String routeId, Direction direction)
    {
        return routeId.equals(routeKey) && direction == directionKey ? stops : null;
    }

    @Override
    public Collection<Prediction> getPredictions(String routeId, int stopId)
    {
        return routeId.equals(routeKey) && stopId == stopKey ? predictions : null;
    }
}