package com.weizilla.transit.bus.favorites;

import com.weizilla.transit.bus.data.Route;

import java.util.Collection;

public class BusFavoriteStoreStub implements BusFavoriteStore
{
    private Collection<Route> routes;

    public BusFavoriteStoreStub(Collection<Route> routes)
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
