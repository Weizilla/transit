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

    public Collection<Direction> getDirections(String routeId)
    {
        return dataSource.getDirections(routeId);
    }

    public Collection<Stop> getStops(String routeId, Direction direction)
    {
        return dataSource.getStops(routeId, direction);
    }

    public Collection<Prediction> getPredictions(String routeId, int stopId)
    {
        return dataSource.getPredictions(routeId, stopId);
    }

    public void saveFavorite(Route route)
    {
        favoritesStore.saveFavorite(route);
    }

    public Collection<String> getFavoriteRouteIds()
    {
        return favoritesStore.getFavoriteRouteIds();
    }

    public void saveFavorite(Stop stop)
    {
        favoritesStore.saveFavorite(stop);
    }

    public Collection<Integer> getFavoriteStopIds(String route, Direction direction)
    {
        return favoritesStore.getFavoriteStopIds(route, direction);
    }

    public Group createGroup(String name)
    {
        return groupsStore.createGroup(name);
    }

    public void renameGroup(int id, String newName)
    {
        groupsStore.renameGroup(id, newName);
    }

    public Collection<Group> getAllGroups()
    {
        return groupsStore.getAllGroups();
    }

    public void deleteGroup(int id)
    {
        groupsStore.deleteGroup(id);
    }

    public void addStopToGroup(int groupId, Stop stop)
    {
        groupsStore.addToGroup(groupId, stop);
    }

    public void removeStopFromGroup(int groupId, int stopId)
    {
        groupsStore.removeFromGroup(groupId, stopId);
    }

    public Collection<Stop> getStops(int groupId)
    {
        return groupsStore.getStops(groupId);
    }
}
