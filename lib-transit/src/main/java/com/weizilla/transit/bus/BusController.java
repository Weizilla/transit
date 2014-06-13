package com.weizilla.transit.bus;

import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.favorites.BusFavoriteStore;
import com.weizilla.transit.bus.source.BusDataSource;

import java.util.Collection;

public class BusController
{
    private final BusDataSource dataSource;
    private final BusFavoriteStore favoriteStore;

    public BusController(BusDataSource dataSource, BusFavoriteStore favoriteStore)
    {
        this.dataSource = dataSource;
        this.favoriteStore = favoriteStore;
    }

    public Collection<Route> getRoutes()
    {
        return dataSource.getRoutes();
    }

    public void saveFavorite(Route route)
    {
        favoriteStore.saveFavorite(route);
    }

    public Collection<Route> getFavoriteRoutes()
    {
        return favoriteStore.getFavoriteRoutes();
    }
}
