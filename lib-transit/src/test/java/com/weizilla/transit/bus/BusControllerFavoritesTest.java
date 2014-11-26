package com.weizilla.transit.bus;

import com.weizilla.transit.BusController;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.favorites.FavoritesStore;
import com.weizilla.transit.favorites.FavoritesStoreStub;
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
        FavoritesStore favoriteStore = mock(FavoritesStore.class);
        BusController controller = new BusController(null, favoriteStore, null);

        controller.saveFavorite(route);

        verify(favoriteStore).saveFavorite(route);
    }

    @Test
    public void getsAllFavoriteRouteIds() throws Exception
    {
        Collection<String> routes = Collections.singletonList("20");

        FavoritesStore favoriteStore = FavoritesStoreStub.createWithRoutes(routes);
        BusController controller = new BusController(null, favoriteStore, null);

        Collection<String> actual = controller.getFavoriteRouteIds();
        assertSame(routes, actual);
    }

    @Test
    public void storesFavoriteStops() throws Exception
    {
        Stop stop = new Stop();
        FavoritesStore favoritesStore = mock(FavoritesStore.class);
        BusController controller = new BusController(null, favoritesStore, null);

        controller.saveFavorite(stop);

        verify(favoritesStore).saveFavorite(stop);
    }

    @Test
    public void getsAllFavoriteStopIds() throws Exception
    {
        Collection<Integer> stops = Collections.singletonList(100);

        FavoritesStore favoritesStore = FavoritesStoreStub.createWithStops(stops);
        BusController controller = new BusController(null, favoritesStore, null);

        Collection<Integer> actual = controller.getFavoriteStopIds(null, null);
        assertSame(stops, actual);
    }
}
