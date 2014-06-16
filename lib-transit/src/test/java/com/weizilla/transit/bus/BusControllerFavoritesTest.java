package com.weizilla.transit.bus;

import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.favorites.BusFavoritesStore;
import com.weizilla.transit.favorites.BusFavoritesStoreStub;
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
        BusFavoritesStore favoriteStore = mock(BusFavoritesStore.class);
        BusController controller = new BusController(null, favoriteStore);

        controller.saveFavorite(route);

        verify(favoriteStore).saveFavorite(route);
    }

    @Test
    public void getsAllFavoriteRoutes() throws Exception
    {
        Collection<Route> routes = Collections.singletonList(new Route());

        BusFavoritesStore favoriteStore = new BusFavoritesStoreStub(routes);
        BusController controller = new BusController(null, favoriteStore);

        Collection<Route> actual = controller.getFavoriteRoutes();
        assertSame(routes, actual);
    }
}
