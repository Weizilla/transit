package com.weizilla.transit.cache;

import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryCacheStore implements CacheStore
{
    private final Map<String, Route> routes;
    private final Map<Integer, Stop> stops;

    public MemoryCacheStore()
    {
        this(new HashMap<String, Route>(), new HashMap<Integer, Stop>());
    }

    public MemoryCacheStore(Map<String, Route> routes, Map<Integer, Stop> stops)
    {
        this.routes = routes;
        this.stops = stops;
    }

    @Override
    public Collection<Stop> getStops(Collection<Integer> stopIds)
    {
        return filter(stops, stopIds);
    }

    @Override
    public Collection<Route> getRoutes(Collection<String> routeIds)
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

    private static <K, V> Collection<V> filter(Map<K, V> map, Collection<K> keys)
    {
        List<V> filtered = new ArrayList<>(keys.size());
        for (K key : keys)
        {
            V value = map.get(key);
            if (value != null)
            {
                filtered.add(value);
            }
        }
        return Collections.unmodifiableList(filtered);
    }
}
