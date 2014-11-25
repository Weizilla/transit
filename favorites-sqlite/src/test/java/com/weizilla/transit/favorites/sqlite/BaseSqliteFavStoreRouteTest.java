package com.weizilla.transit.favorites.sqlite;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.favorites.BusFavoritesStore;
import com.weizilla.transit.sqlite.BaseSqliteTest;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.weizilla.transit.favorites.sqlite.Favorites.RouteEntry.TABLE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class BaseSqliteFavStoreRouteTest extends BaseSqliteTest
{
    protected BusFavoritesStore store;

    @Test
    public void storeIsSet() throws Exception
    {
        assertNotNull(store);
    }

    @Test
    public void getRouteIdsReturnsDbData() throws Exception
    {
        loadIntoDb("get_routes.xml");
        Set<String> expected = Sets.newHashSet("22", "36", "54A");

        Collection<String> actualIds = store.getFavoriteRouteIds();
        assertEquals(expected, new HashSet<>(actualIds));
    }

    @Test
    public void savesFavoriteRoutes() throws Exception
    {
        String dataSetFile = "save_routes.xml";
        deleteFromDb(dataSetFile);

        Collection<String> routeIds = Lists.newArrayList("134", "156", "J14");
        for (String routeId : routeIds)
        {
            store.saveFavorite(new Route(routeId));
        }

        assertTablesEqualFile(dataSetFile, TABLE_NAME);
    }

}