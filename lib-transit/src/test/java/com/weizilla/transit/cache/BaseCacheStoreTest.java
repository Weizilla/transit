package com.weizilla.transit.cache;

import com.google.common.collect.Lists;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.utils.Mapper;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.weizilla.transit.utils.Asserts.assertEqualsRoutes;
import static com.weizilla.transit.utils.Asserts.assertEqualsStops;
import static org.junit.Assert.assertNotNull;

public abstract class BaseCacheStoreTest
{
    protected static final Route ROUTE_1 = new Route("100", "ROUTE A");
    protected static final Route ROUTE_2 = new Route("200", "ROUTE B");
    protected static final Route ROUTE_3 = new Route("300", "ROUTE C");
    protected static final Map<String, Route> ROUTES = Mapper.toMap(ROUTE_1, ROUTE_2, ROUTE_3);
    protected static final Stop STOP_1 = new Stop(100, "STOP A");
    protected static final Stop STOP_2 = new Stop(200, "STOP B");
    protected static final Stop STOP_3 = new Stop(300, "STOP C");
    protected static final Map<Integer, Stop> STOPS = Mapper.toMap(STOP_1, STOP_2, STOP_3);
    protected CacheStore store;

    @Test
    public void storeIsSet() throws Exception
    {
        assertNotNull(store);
    }

    @Test
    public void returnsRoutesAfterUpdate() throws Exception
    {
        store.updateRoutes(ROUTES.values());

        List<String> routeIds = Lists.newArrayList(ROUTE_1.getId(), ROUTE_2.getId(), "NOT FOUND ID");
        List<Route> expected = Lists.newArrayList(ROUTE_1, ROUTE_2);
        Collection<Route> actual = store.getRoutes(routeIds);

        assertEqualsRoutes(expected, actual);
    }

    @Test
    public void returnsStopsAfterUpdate() throws Exception
    {
        store.updateStops(STOPS.values());

        List<Integer> stopIds = Lists.newArrayList(STOP_1.getId(), STOP_2.getId(), 10000);
        List<Stop> expected = Lists.newArrayList(STOP_1, STOP_2);
        Collection<Stop> actual = store.getStops(stopIds);

        assertEqualsStops(expected, actual);
    }
}
