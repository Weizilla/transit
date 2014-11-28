package com.weizilla.transit;

import com.weizilla.transit.cache.CacheStore;
import com.weizilla.transit.cache.MemoryCacheStore;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.favorites.FavoritesStore;
import com.weizilla.transit.favorites.FavoritesStoreStub;
import com.weizilla.transit.groups.GroupsStore;
import com.weizilla.transit.groups.GroupsStoreStub;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.weizilla.transit.utils.Asserts.assertEqualsStops;
import static com.weizilla.transit.utils.Mapper.toMap;

public class BusControllerStopTest
{
    private static final Stop STOP_1 = new Stop(10, "STOP A");
    private static final Stop STOP_2 = new Stop(20, "STOP B");
    private static final Stop STOP_3 = new Stop(30, "STOP C");
    private static final Map<Integer, Stop> STOP_MAP = toMap(STOP_1, STOP_2, STOP_3);
    private static final Set<Integer> STOP_IDS = STOP_MAP.keySet();

    @Test
    public void looksUpStopsFromGroupsInCache() throws Exception
    {
        int groupId = 10;
        GroupsStore groupsStore = new GroupsStoreStub(groupId, STOP_IDS);
        CacheStore cacheStore = new MemoryCacheStore(null, STOP_MAP);
        BusController controller = new BusController(null, null, groupsStore, cacheStore);

        Collection<Stop> actual = controller.getStopsForGroup(groupId);
        assertEqualsStops(STOP_MAP.values(), actual);
    }

    @Test
    public void looksUpFavoriteStopsInCache() throws Exception
    {
        FavoritesStore favoritesStore = new FavoritesStoreStub(null, STOP_IDS);
        CacheStore cacheStore = new MemoryCacheStore(null, STOP_MAP);
        BusController controller = new BusController(null, favoritesStore, null, cacheStore);

        Collection<Stop> actual = controller.getFavoriteStops();
        assertEqualsStops(STOP_MAP.values(), actual);
    }
}
