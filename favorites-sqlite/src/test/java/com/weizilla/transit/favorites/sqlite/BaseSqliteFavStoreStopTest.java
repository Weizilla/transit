package com.weizilla.transit.favorites.sqlite;

import com.google.common.collect.Sets;
import com.weizilla.transit.favorites.FavoritesStore;
import com.weizilla.transit.sqlite.BaseSqliteTest;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.weizilla.transit.favorites.sqlite.Favorites.StopEntry.TABLE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class BaseSqliteFavStoreStopTest extends BaseSqliteTest
{
    protected FavoritesStore store;

    @Test
    public void storeIsSet() throws Exception
    {
        assertNotNull(store);
    }

    @Test
    public void getStopIdsReturnsDbData() throws Exception
    {
        Set<Integer> expected = Sets.newHashSet(100, 200, 300, 400);
        loadIntoDb("favorites/get_stops.xml");

        Collection<Integer> actualIds = store.getStopIds();
        assertEquals(expected, new HashSet<>(actualIds));
    }

    @Test
    public void savesFavoriteStops() throws Exception
    {
        String dataSetFile = "favorites/save_stops.xml";
        deleteFromDb(dataSetFile);

        store.saveStop(100);
        store.saveStop(200);
        store.saveStop(300);
        store.saveStop(400);

        assertTablesEqualFile(dataSetFile, TABLE_NAME);
    }
}