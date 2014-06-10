package com.weizilla.transit.bus.source;

import com.weizilla.transit.bus.data.Route;

import java.util.Collection;

public interface BusSource
{
    Collection<Route> getRoutes();
}
