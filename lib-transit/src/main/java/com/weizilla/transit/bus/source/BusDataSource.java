package com.weizilla.transit.bus.source;

import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;

import java.util.Collection;

public interface BusDataSource
{
    Collection<Route> getRoutes();
    Collection<Direction> getDirections(String route);
    Collection<Stop> getStops(String route, Direction direction);
}
