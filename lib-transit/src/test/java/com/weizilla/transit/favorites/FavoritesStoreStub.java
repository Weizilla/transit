package com.weizilla.transit.favorites;

import java.util.Collection;

public class FavoritesStoreStub implements FavoritesStore
{
    private Collection<String> routes;
    private Collection<Integer> stops;

    private FavoritesStoreStub(Collection<String> routes, Collection<Integer> stops)
    {
        this.routes = routes;
        this.stops = stops;
    }

    public static FavoritesStoreStub createWithRoutes(Collection<String> routes)
    {
        return new FavoritesStoreStub(routes, null);
    }

    public static FavoritesStoreStub createWithStops(Collection<Integer> stops)
    {
        return new FavoritesStoreStub(null, stops);
    }

    @Override
    public void saveRoute(String id)
    {
        // do nothing
    }

    @Override
    public Collection<String> getRouteIds()
    {
        return routes;
    }

    @Override
    public void saveStop(int id)
    {
        // do nothing
    }

    @Override
    public Collection<Integer> getStopIds()
    {
        return stops;
    }
}
