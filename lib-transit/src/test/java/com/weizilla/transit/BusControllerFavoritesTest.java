package com.weizilla.transit;

import com.weizilla.transit.favorites.FavoritesStore;
import org.junit.Test;

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
    public void storesFavoriteStops() throws Exception
    {
        FavoritesStore favoritesStore = mock(FavoritesStore.class);
        BusController controller = new BusController(null, favoritesStore, null, null);

        controller.saveFavoriteStop(STOP_ID);

        verify(favoritesStore).saveStop(STOP_ID);
    }
}
