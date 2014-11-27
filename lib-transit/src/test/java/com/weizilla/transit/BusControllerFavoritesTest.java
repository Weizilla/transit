package com.weizilla.transit;

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
    private static final String ROUTE_ID = "22";
    private static final int STOP_ID = 100;

    @Test
    public void storesFavoriteRoutes() throws Exception
    {
        FavoritesStore favoriteStore = mock(FavoritesStore.class);
        BusController controller = new BusController(null, favoriteStore, null, null);

        controller.saveFavoriteRoute(ROUTE_ID);

        verify(favoriteStore).saveRoute(ROUTE_ID);
    }

    @Test
    public void getsAllFavoriteRouteIds() throws Exception
    {
        Collection<String> routes = Collections.singletonList(ROUTE_ID);

        FavoritesStore favoriteStore = FavoritesStoreStub.createWithRoutes(routes);
        BusController controller = new BusController(null, favoriteStore, null, null);

        Collection<String> actual = controller.getFavoriteRouteIds();
        assertSame(routes, actual);
    }

    @Test
    public void storesFavoriteStops() throws Exception
    {
        FavoritesStore favoritesStore = mock(FavoritesStore.class);
        BusController controller = new BusController(null, favoritesStore, null, null);

        controller.saveFavoriteStop(STOP_ID);

        verify(favoritesStore).saveStop(STOP_ID);
    }

    @Test
    public void getsAllFavoriteStopIds() throws Exception
    {
        Collection<Integer> stops = Collections.singletonList(STOP_ID);

        FavoritesStore favoritesStore = FavoritesStoreStub.createWithStops(stops);
        BusController controller = new BusController(null, favoritesStore, null, null);

        Collection<Integer> actual = controller.getFavoriteStopIds();
        assertSame(stops, actual);
    }
}
