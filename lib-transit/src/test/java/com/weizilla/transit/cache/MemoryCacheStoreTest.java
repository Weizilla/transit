package com.weizilla.transit.cache;

import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MemoryCacheStoreTest
{
    private static final Map<String, Route> ROUTES = createRoutes();
    private static final Map<Integer, Stop> STOPS = createStops();
    private CacheStore cacheStore;

    @Before
    public void setUp() throws Exception
    {
        cacheStore = new MemoryCacheStore();
    }

    @Test
    public void returnsRoutesAfterUpdate() throws Exception
    {
        cacheStore.updateRoutes(ROUTES.values());
        Map<String, Route> actual = cacheStore.getRoutes(ROUTES.keySet());
        assertEquals(ROUTES, actual);
    }

    @Test
    public void returnsStopsAfterUpdate() throws Exception
    {
        cacheStore.updateStops(STOPS.values());
        Map<Integer, Stop> actual = cacheStore.getStops(STOPS.keySet());
        assertEquals(STOPS, actual);
    }

    private static Map<String, Route> createRoutes()
    {
        Map<String, Route> routes = new HashMap<>();
        routes.put("100", new Route("100", "ROUTE A"));
        routes.put("200", new Route("200", "ROUTE A"));
        routes.put("300", new Route("300", "ROUTE A"));
        return routes;
    }

    private static Map<Integer, Stop> createStops()
    {
        Map<Integer, Stop> stops = new HashMap<>();
        stops.put(100, new Stop(100, "STOP A"));
        stops.put(200, new Stop(200, "STOP B"));
        stops.put(300, new Stop(300, "STOP C"));
        return stops;
    }
}