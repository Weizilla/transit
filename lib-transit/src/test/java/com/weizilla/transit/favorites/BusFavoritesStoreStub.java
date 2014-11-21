package com.weizilla.transit.favorites;

import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;

import java.util.Collection;

public class BusFavoritesStoreStub implements BusFavoritesStore
{
    private Collection<String> routes;
    private Collection<Integer> stops;

    private BusFavoritesStoreStub(Collection<String> routes, Collection<Integer> stops)
    {
        this.routes = routes;
        this.stops = stops;
    }

    public static BusFavoritesStoreStub createWithRoutes(Collection<String> routes)
    {
        return new BusFavoritesStoreStub(routes, null);
    }

    public static BusFavoritesStoreStub createWithStops(Collection<Integer> stops)
    {
        return new BusFavoritesStoreStub(null, stops);
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
