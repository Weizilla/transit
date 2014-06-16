package com.weizilla.transit.favorites;

import com.weizilla.transit.bus.data.Route;

import java.util.Collection;

public class BusFavoritesStoreStub implements BusFavoritesStore
{
    private Collection<Route> routes;

    public BusFavoritesStoreStub(Collection<Route> routes)
    {
        this.routes = routes;
    }

    @Override
    public void saveFavorite(Route route)
    {
        // do nothing
    }

    @Override
    public Collection<Route> getFavoriteRoutes()
    {
        return routes;
    }
}
