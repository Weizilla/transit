package com.weizilla.transit.bus;

import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.favorites.BusFavoriteStore;
import com.weizilla.transit.favorites.BusFavoriteStoreStub;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BusControllerFavoritesTest
{
    @Test
    public void storesFavoriteRoutes() throws Exception
    {
        Route route = new Route();
        BusFavoriteStore favoriteStore = mock(BusFavoriteStore.class);
        BusController controller = new BusController(null, favoriteStore);

        controller.saveFavorite(route);

        verify(favoriteStore).saveFavorite(route);
    }

    @Test
    public void getsAllFavoriteRoutes() throws Exception
    {
        Collection<Route> routes = Collections.singletonList(new Route());

        BusFavoriteStore favoriteStore = new BusFavoriteStoreStub(routes);
        BusController controller = new BusController(null, favoriteStore);

        Collection<Route> actual = controller.getFavoriteRoutes();
        assertSame(routes, actual);
    }
}
