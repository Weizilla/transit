package com.weizilla.transit;

import com.weizilla.transit.cache.CacheStore;
import com.weizilla.transit.cache.MemoryCacheStore;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.favorites.FavoritesStore;
import com.weizilla.transit.favorites.FavoritesStoreStub;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

import static com.weizilla.transit.utils.Asserts.assertEqualsRoutes;
import static com.weizilla.transit.utils.Mapper.toMap;

public class BusControllerRouteTest
{
    private static final Route ROUTE_1 = new Route("10", "ROUTE A");
    private static final Route ROUTE_2 = new Route("20", "ROUTE B");
    private static final Route ROUTE_3 = new Route("30", "ROUTE C");
    private static final Map<String, Route> ROUTE_MAP = toMap(ROUTE_1, ROUTE_2, ROUTE_3);

    @Test
    public void looksUpFavoriteRoutesInCache() throws Exception
    {
        FavoritesStore favoritesStore = new FavoritesStoreStub(ROUTE_MAP.keySet(), null);
        CacheStore cacheStore = new MemoryCacheStore(ROUTE_MAP, null);
        BusController controller = new BusController(null, favoritesStore, null, cacheStore);

        Collection<Route> actual = controller.getFavoriteRoutes();
        assertEqualsRoutes(ROUTE_MAP.values(), actual);
    }
}
