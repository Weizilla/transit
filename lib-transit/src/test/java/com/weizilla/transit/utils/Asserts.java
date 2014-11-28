package com.weizilla.transit.utils;

import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.Assert.assertEquals;

public class Asserts
{
    private Asserts()
    {
        // util class
    }

    public static void assertEqualsRoutes(Collection<Route> expected, Collection<Route> actual)
    {
        assertEquals(expected.size(), actual.size());
        Map<String, Route> expectedMap = Mapper.toRoutesMap(expected);
        Map<String, Route> actualMap = Mapper.toRoutesMap(actual);

        for (Entry<String, Route> expectedEntry : expectedMap.entrySet())
        {
            Route actualRoute = actualMap.get(expectedEntry.getKey());
            Route expectedRoute = expectedEntry.getValue();
            assertEquals(expectedRoute.getId(), actualRoute.getId());
            assertEquals(expectedRoute.getName(), actualRoute.getName());
        }
    }

    public static void assertEqualsStops(Collection<Stop> expected, Collection<Stop> actual)
    {
        assertEquals(expected.size(), actual.size());
        Map<Integer, Stop> expectedMap = Mapper.toStopsMap(expected);
        Map<Integer, Stop> actualMap = Mapper.toStopsMap(actual);

        for (Entry<Integer, Stop> expectedEntry : expectedMap.entrySet())
        {
            Stop actualStop = actualMap.get(expectedEntry.getKey());
            Stop expectedStop = expectedEntry.getValue();
            assertEquals(expectedStop.getId(), actualStop.getId());
            assertEquals(expectedStop.getName(), actualStop.getName());
        }
    }
}
