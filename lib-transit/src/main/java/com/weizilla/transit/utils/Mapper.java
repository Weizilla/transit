package com.weizilla.transit.utils;

import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Mapper
{
    private Mapper() {
        // private
    }

    public static Map<String, Route> toRoutesMap(Collection<Route> routes)
    {
        Map<String, Route> routeMap = new HashMap<>(routes.size());
        for (Route route : routes)
        {
            routeMap.put(route.getId(), route);
        }
        return routeMap;
    }

    public static Map<String, Route> toMap(Route ... routes)
    {
        return toRoutesMap(Arrays.asList(routes));
    }

    public static Map<Integer, Stop> toStopsMap(Collection<Stop> stops)
    {
        Map<Integer, Stop> stopMap = new HashMap<>(stops.size());
        for (Stop stop : stops)
        {
            stopMap.put(stop.getId(), stop);
        }
        return stopMap;
    }

    public static Map<Integer, Stop> toMap(Stop ... stops)
    {
        return toStopsMap(Arrays.asList(stops));
    }
}
