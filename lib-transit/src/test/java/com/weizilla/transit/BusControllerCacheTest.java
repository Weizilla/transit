package com.weizilla.transit;

import com.google.common.collect.Lists;
import com.weizilla.transit.cache.CacheStore;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.source.DataSource;
import com.weizilla.transit.source.DataSourceStub;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BusControllerCacheTest
{
    private static final List<Stop> STOPS = Lists.newArrayList(
        new Stop(100, "STOP A"), new Stop(200, "STOP B"));
    private static final List<Route> ROUTES = Lists.newArrayList(
        new Route("10", "ROUTE A"), new Route("20", "ROUTE B"));
    private CacheStore mockCacheStore;
    private BusController controller;
    private DataSource dataSource;

    @Before
    public void setUp() throws Exception
    {
        mockCacheStore = mock(CacheStore.class);
        dataSource = new DataSourceStub(ROUTES, STOPS);
        controller = new BusController(dataSource, null, null, mockCacheStore);
    }

    @Test
    public void updatesRoutesFromDataSource() throws Exception
    {
        controller.getRoutes();
        verify(mockCacheStore).updateRoutes(ROUTES);
    }

    @Test
    public void updatesStopsFromDataSource() throws Exception
    {
        controller.getStops(null, null);
        verify(mockCacheStore).updateStops(STOPS);
    }

    @Test
    public void looksUpRoutesInCache() throws Exception
    {
        List<String> routeIds = Lists.newArrayList("10", "20");
        controller.lookupRoutes(routeIds);
        verify(mockCacheStore).getRoutes(routeIds);
    }

    @Test
    public void looksUpStopInCache() throws Exception
    {
        List<Integer> stopIds = Lists.newArrayList(100, 200);
        controller.lookupStops(stopIds);
        verify(mockCacheStore).getStops(stopIds);
    }
}
