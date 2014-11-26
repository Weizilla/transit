package com.weizilla.transit;

import com.weizilla.transit.data.Direction;
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
    private static final Direction DIRECTION = Direction.Eastbound;

    @Test
    public void storesFavoriteRoutes() throws Exception
    {
        FavoritesStore favoriteStore = mock(FavoritesStore.class);
        BusController controller = new BusController(null, favoriteStore, null);

        controller.saveFavorite(ROUTE_ID);

        verify(favoriteStore).saveFavorite(ROUTE_ID);
    }

    @Test
    public void getsAllFavoriteRouteIds() throws Exception
    {
        Collection<String> routes = Collections.singletonList(ROUTE_ID);

        FavoritesStore favoriteStore = FavoritesStoreStub.createWithRoutes(routes);
        BusController controller = new BusController(null, favoriteStore, null);

        Collection<String> actual = controller.getFavoriteRouteIds();
        assertSame(routes, actual);
    }

    @Test
    public void storesFavoriteStops() throws Exception
    {
        FavoritesStore favoritesStore = mock(FavoritesStore.class);
        BusController controller = new BusController(null, favoritesStore, null);

        controller.saveFavorite(STOP_ID, ROUTE_ID, DIRECTION);

        verify(favoritesStore).saveFavorite(STOP_ID, ROUTE_ID, DIRECTION);
    }

    @Test
    public void getsAllFavoriteStopIds() throws Exception
    {
        Collection<Integer> stops = Collections.singletonList(STOP_ID);

        FavoritesStore favoritesStore = FavoritesStoreStub.createWithStops(stops);
        BusController controller = new BusController(null, favoritesStore, null);

        Collection<Integer> actual = controller.getFavoriteStopIds(null, null);
        assertSame(stops, actual);
    }
}
