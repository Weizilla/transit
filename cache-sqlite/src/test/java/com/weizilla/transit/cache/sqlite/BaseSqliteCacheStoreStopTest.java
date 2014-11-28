package com.weizilla.transit.cache.sqlite;

import com.google.common.collect.Lists;
import com.weizilla.transit.cache.CacheStore;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.sqlite.BaseSqliteTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.weizilla.transit.cache.sqlite.Cache.StopEntry.TABLE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public abstract class BaseSqliteCacheStoreStopTest extends BaseSqliteTest
{
    private static final Stop STOP_1 = new Stop(10, "STOP A");
    private static final Stop STOP_2 = new Stop(20, "STOP B");
    private static final Stop STOP_3 = new Stop(30, "STOP C");
    private static final List<Stop> STOPS = Lists.newArrayList(STOP_1, STOP_2, STOP_3);
    protected CacheStore store;

    @Test
    public void storeIsSet() throws Exception
    {
        assertNotNull(store);
    }

    @Test
    public void updatesDbWithNewData() throws Exception
    {
        String dataSetFile = "cache/update_stops_before.xml";
        deleteFromDb(dataSetFile);
        store.updateStops(STOPS);
        assertTablesEqualFile(dataSetFile, TABLE_NAME);
    }

    @Test
    public void returnsMapFromDb() throws Exception
    {
        String dataSetFile = "cache/update_stops_before.xml";
        loadIntoDb(dataSetFile);

        Map<Integer, Stop> expected = new HashMap<>();
        expected.put(STOP_1.getId(), STOP_1);
        expected.put(STOP_2.getId(), STOP_2);

        Collection<Integer> stopIds = new ArrayList<>(expected.keySet());
        stopIds.add(1000);

        Map<Integer, Stop> actual = store.getStops(stopIds);

        assertEquals(expected.size(), actual.size());
        assertStop(STOP_1.getId(), expected, actual);
        assertStop(STOP_2.getId(), expected, actual);
    }

    @Test
    public void updatesDbReplacingOldData() throws Exception
    {
        loadIntoDb("cache/update_stops_before.xml");

        List<Stop> newStops = Lists.newArrayList(
            STOP_1,
            STOP_2,
            new Stop(STOP_3.getId(), "NEW NAME"),
            new Stop(200, "BRAND NEW")
        );

        store.updateStops(newStops);
        assertTablesEqualFile("cache/update_stops_after.xml", TABLE_NAME);
    }

    @Test
    public void returnsEmptyMapForEmptyInput() throws Exception
    {
        loadIntoDb("cache/update_stops_before.xml");
        assertTrue(store.getStops(Collections.<Integer>emptyList()).isEmpty());
    }

    @Test
    public void keepsDataIfUpdatingWithEmptyList() throws Exception
    {
        String dataSetFile = "cache/update_stops_before.xml";
        loadIntoDb(dataSetFile);
        store.updateStops(Collections.<Stop>emptyList());
        assertTablesEqualFile(dataSetFile, TABLE_NAME);
    }

    private static void assertStop(int id, Map<Integer, Stop> expected, Map<Integer, Stop> actual)
    {
        assertEquals(expected.get(id).getId(), actual.get(id).getId());
        assertEquals(expected.get(id).getName(), actual.get(id).getName());
    }
}
