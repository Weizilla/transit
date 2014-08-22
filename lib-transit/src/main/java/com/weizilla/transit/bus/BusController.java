package com.weizilla.transit.bus;

import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Prediction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;
import com.weizilla.transit.bus.source.BusDataSource;
import com.weizilla.transit.favorites.BusFavoritesStore;

import java.util.Collection;

public class BusController
{
    private final BusDataSource dataSource;
    private final BusFavoritesStore favoriteStore;

    public BusController(BusDataSource dataSource, BusFavoritesStore favoriteStore)
    {
        this.dataSource = dataSource;
        this.favoriteStore = favoriteStore;
    }

    public Collection<Route> getRoutes()
    {
        return dataSource.getRoutes();
    }

    public Collection<Direction> getDirections(Route route)
    {
        return dataSource.getDirections(route);
    }

    public Collection<Stop> getStops(Route route, Direction direction)
    {
        return dataSource.getStops(route, direction);
    }

    public Collection<Prediction> getPredictions(Route route, Stop stop)
    {
        return dataSource.getPredictions(route, stop);
    }

    public void saveFavorite(Route route)
    {
        favoriteStore.saveFavorite(route);
    }

    public Collection<String> getFavoriteRoutes()
    {
        return favoriteStore.getFavoriteRoutes();
    }

    public void saveFavorite(Stop stop)
    {
        favoriteStore.saveFavorite(stop);
    }

    public Collection<Integer> getFavoriteStops(String route, Direction direction)
    {
        return favoriteStore.getFavoriteStops(route, direction);
    }
}
