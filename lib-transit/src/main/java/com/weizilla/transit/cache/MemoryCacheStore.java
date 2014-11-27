package com.weizilla.transit.cache;

import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MemoryCacheStore implements CacheStore
{
    private final Map<Integer, Stop> stops = new HashMap<>();
    private final Map<String, Route> routes = new HashMap<>();

    @Override
    public Map<Integer, Stop> getStops(Collection<Integer> stopIds)
    {
        return filter(stops, stopIds);
    }

    @Override
    public Map<String, Route> getRoutes(Collection<String> routeIds)
    {
        return filter(routes, routeIds);
    }

    @Override
    public void updateRoutes(Collection<Route> routes)
    {
        for (Route route : routes)
        {
            this.routes.put(route.getId(), route);
        }
    }

    @Override
    public void updateStops(Collection<Stop> stops)
    {
        for (Stop stop : stops)
        {
            this.stops.put(stop.getId(), stop);
        }
    }

    private static <K, V> Map<K, V> filter(Map<K, V> map, Collection<K> keys)
    {
        Map<K, V> filtered = new HashMap<>(keys.size());
        for (K key : keys)
        {
            filtered.put(key, map.get(key));
        }
        return Collections.unmodifiableMap(filtered);
    }
}
