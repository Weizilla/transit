package com.weizilla.transit.bus;

import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;
import com.weizilla.transit.favorites.BusFavoritesStore;
import com.weizilla.transit.favorites.BusFavoritesStoreStub;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BusControllerFavoritesTest
{
    @Test
    public void storesFavoriteRoutes() throws Exception
    {
        Route route = new Route("20");
        BusFavoritesStore favoriteStore = mock(BusFavoritesStore.class);
        BusController controller = new BusController(null, favoriteStore);

        controller.saveFavorite(route);

        verify(favoriteStore).saveFavorite(route);
    }

    @Test
    public void getsAllFavoriteRoutes() throws Exception
    {
        Collection<String> routes = Collections.singletonList("20");

        BusFavoritesStore favoriteStore = BusFavoritesStoreStub.createWithRoutes(routes);
        BusController controller = new BusController(null, favoriteStore);

        Collection<String> actual = controller.getFavoriteRoutes();
        assertSame(routes, actual);
    }

    @Test
    public void storesFavoriteStops() throws Exception
    {
        Stop stop = new Stop();
        BusFavoritesStore favoritesStore = mock(BusFavoritesStore.class);
        BusController controller = new BusController(null, favoritesStore);

        controller.saveFavorite(stop);

        verify(favoritesStore).saveFavorite(stop);
    }

    @Test
    public void getsAllFavoriteStops() throws Exception
    {
        Collection<Integer> stops = Collections.singletonList(100);

        BusFavoritesStore favoritesStore = BusFavoritesStoreStub.createWithStops(stops);
        BusController controller = new BusController(null, favoritesStore);

        Collection<Integer> actual = controller.getFavoriteStops(null, null);
        assertSame(stops, actual);
    }
}
