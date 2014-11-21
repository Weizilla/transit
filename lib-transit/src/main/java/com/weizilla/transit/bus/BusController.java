package com.weizilla.transit.bus;

import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Prediction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;
import com.weizilla.transit.bus.source.BusDataSource;
import com.weizilla.transit.favorites.BusFavoritesStore;
import com.weizilla.transit.groups.BusGroupsStore;
import com.weizilla.transit.groups.Group;

import java.util.Collection;

public class BusController
{
    private final BusDataSource dataSource;
    private final BusFavoritesStore favoritesStore;
    private final BusGroupsStore groupsStore;

    public BusController(BusDataSource dataSource, BusFavoritesStore favoritesStore, BusGroupsStore groupsStore)
    {
        this.dataSource = dataSource;
        this.favoritesStore = favoritesStore;
        this.groupsStore = groupsStore;
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
        favoritesStore.saveFavorite(route);
    }

    public Collection<String> getFavoriteRoutes()
    {
        return favoritesStore.getFavoriteRoutes();
    }

    public void saveFavorite(Stop stop)
    {
        favoritesStore.saveFavorite(stop);
    }

    public Collection<Integer> getFavoriteStops(String route, Direction direction)
    {
        return favoritesStore.getFavoriteStops(route, direction);
    }

    public Group createGroup(String name)
    {
        return groupsStore.createGroup(name);
    }
}
