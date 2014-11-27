package com.weizilla.transit.cache;

import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;

import java.util.Collection;
import java.util.Map;

public interface CacheStore
{
    Map<Integer, Stop> getStops(Collection<Integer> stopIds);
    Map<String, Route> getRoutes(Collection<String> routeIds);
    void updateRoutes(Collection<Route> routes);
    void updateStops(Collection<Stop> stops);
}
