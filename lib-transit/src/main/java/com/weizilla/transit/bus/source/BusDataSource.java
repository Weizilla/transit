package com.weizilla.transit.bus.source;

import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Prediction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;

import java.util.Collection;

public interface BusDataSource
{
    Collection<Route> getRoutes();
    Collection<Direction> getDirections(Route route);
    Collection<Stop> getStops(Route route, Direction direction);
    Collection<Prediction> getPredictions(Route route, Stop stop);
}
