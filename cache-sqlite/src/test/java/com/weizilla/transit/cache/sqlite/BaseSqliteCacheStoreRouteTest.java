package com.weizilla.transit.cache.sqlite;

import com.google.common.collect.Lists;
import com.weizilla.transit.cache.CacheStore;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.sqlite.BaseSqliteTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.weizilla.transit.cache.sqlite.Cache.RouteEntry.TABLE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public abstract class BaseSqliteCacheStoreRouteTest extends BaseSqliteTest
{
    private static final Route ROUTE_1 = new Route("134", "Stockton Express");
    private static final Route ROUTE_2 = new Route("156", "LaSalle" );
    private static final Route ROUTE_3 = new Route("J14", "Jefferson Jump" );
    private static final List<Route> ROUTES = Lists.newArrayList(ROUTE_1, ROUTE_2, ROUTE_3);
    protected CacheStore store;

    @Test
    public void storeIsSet() throws Exception
    {
        assertNotNull(store);
    }

    @Test
    public void updatesDbWithNewData() throws Exception
    {
        String dataSetFile = "cache/update_routes_before.xml";
        deleteFromDb(dataSetFile);
        store.updateRoutes(ROUTES);
        assertTablesEqualFile(dataSetFile, TABLE_NAME);
    }

    @Test
    public void returnsMapFromDb() throws Exception
    {
        String dataSetFile = "cache/update_routes_before.xml";
        loadIntoDb(dataSetFile);

        Map<String, Route> expected = new HashMap<>();
        expected.put(ROUTE_1.getId(), ROUTE_1);
        expected.put(ROUTE_2.getId(), ROUTE_2);

        Collection<String> routeIds = new ArrayList<>(expected.keySet());
        routeIds.add("NOT FOUND ID");

        Map<String, Route> actual = store.getRoutes(routeIds);

        assertEquals(expected.size(), actual.size());
        assertRoute(ROUTE_1.getId(), expected, actual);
        assertRoute(ROUTE_2.getId(), expected, actual);
    }

    @Test
    public void updatesDbReplacingOldData() throws Exception
    {
        loadIntoDb("cache/update_routes_before.xml");

        List<Route> newRoutes = Lists.newArrayList(
            ROUTE_1,
            new Route(ROUTE_3.getId(), "NEW BUS NAME"),
            new Route("22", "Clark")
        );

        store.updateRoutes(newRoutes);
        assertTablesEqualFile("cache/update_routes_after.xml", TABLE_NAME);
    }

    @Test
    public void returnsEmptyMapForEmptyInput() throws Exception
    {
        loadIntoDb("cache/update_routes_before.xml");
        assertTrue(store.getRoutes(Collections.<String>emptyList()).isEmpty());
    }

    @Test
    public void keepsDataIfUpdatingWithEmptyList() throws Exception
    {
        String dataSetFile = "cache/update_routes_before.xml";
        loadIntoDb(dataSetFile);
        store.updateRoutes(Collections.<Route>emptyList());
        assertTablesEqualFile(dataSetFile, TABLE_NAME);
    }

    private static void assertRoute(String id, Map<String, Route> expected, Map<String, Route> actual)
    {
        assertEquals(expected.get(id).getId(), actual.get(id).getId());
        assertEquals(expected.get(id).getName(), actual.get(id).getName());
    }
}