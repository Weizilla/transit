package com.weizilla.transit.favorites;

import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;

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
    public void saveFavorite(Route route)
    {
        // do nothing
    }

    @Override
    public Collection<String> getFavoriteRouteIds()
    {
        return routes;
    }

    @Override
    public void saveFavorite(Stop stop)
    {
        // do nothing
    }

    @Override
    public Collection<Integer> getFavoriteStopIds(String route, Direction direction)
    {
        return stops;
    }
}
