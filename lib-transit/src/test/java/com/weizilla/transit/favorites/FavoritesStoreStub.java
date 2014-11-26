package com.weizilla.transit.favorites;

import com.weizilla.transit.data.Direction;

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
    public void saveFavorite(String routeId)
    {
        // do nothing
    }

    @Override
    public Collection<String> getRouteIds()
    {
        return routes;
    }

    @Override
    public void saveFavorite(int stopId, String routeId, Direction direction)
    {
        // do nothing
    }

    @Override
    public Collection<Integer> getStopIds(String routeId, Direction direction)
    {
        return stops;
    }
}
