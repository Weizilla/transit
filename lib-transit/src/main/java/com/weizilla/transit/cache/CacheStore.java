package com.weizilla.transit.cache;

import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;

import java.util.Collection;

public interface CacheStore
{
    Collection<Stop> getStops(Collection<Integer> stopIds);
    Collection<Route> getRoutes(Collection<String> routeIds);
    void updateRoutes(Collection<Route> routes);
    void updateStops(Collection<Stop> stops);
}
