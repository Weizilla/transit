package com.weizilla.transit.utils;

import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MapperTest
{

    private static final Route ROUTE_1 = new Route("134", "Stockton Express");
    private static final Route ROUTE_2 = new Route("156", "LaSalle");
    private static final Route ROUTE_3 = new Route("J14", "Jefferson Jump");
    private static final Map<String, Route> ROUTES = new HashMap<>();
    private static final Stop STOP_1 = new Stop(10, "STOP A");
    private static final Stop STOP_2 = new Stop(20, "STOP B");
    private static final Stop STOP_3 = new Stop(30, "STOP C");
    private static final Map<Integer, Stop> STOPS = new HashMap<>();
    static {
        ROUTES.put(ROUTE_1.getId(), ROUTE_1);
        ROUTES.put(ROUTE_2.getId(), ROUTE_2);
        ROUTES.put(ROUTE_3.getId(), ROUTE_3);
        STOPS.put(STOP_1.getId(), STOP_1);
        STOPS.put(STOP_2.getId(), STOP_2);
        STOPS.put(STOP_3.getId(), STOP_3);
    }

    @Test
    public void createsRouteMapFromCollection() throws Exception
    {
        Map<String, Route> actual = Mapper.toRoutesMap(ROUTES.values());
        assertEquals(ROUTES, actual);
    }

    @Test
    public void createsRoutesMapFromArray() throws Exception
    {
        Map<String, Route> actual = Mapper.toMap(ROUTE_1, ROUTE_2, ROUTE_3);
        assertEquals(ROUTES, actual);
    }

    @Test
    public void createsStopMapFromCollection() throws Exception
    {
        Map<Integer, Stop> actual = Mapper.toStopsMap(STOPS.values());
        assertEquals(STOPS, actual);
    }

    @Test
    public void createsStopsMapFromArray() throws Exception
    {
        Map<Integer, Stop> actual = Mapper.toMap(STOP_1, STOP_2, STOP_3);
        assertEquals(STOPS, actual);
    }
}