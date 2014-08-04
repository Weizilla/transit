package com.weizilla.transit.favorites;

import com.weizilla.transit.bus.data.Route;

import java.util.Collection;

public class BusFavoritesStoreStub implements BusFavoritesStore
{
    private Collection<String> routes;

    public BusFavoritesStoreStub(Collection<String> routes)
    {
        this.routes = routes;
    }

    @Override
    public void saveFavorite(Route route)
    {
        // do nothing
    }

    @Override
    public Collection<String> getFavoriteRoutes()
    {
        return routes;
    }
}
