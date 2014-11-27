package com.weizilla.transit;

import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Prediction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.favorites.FavoritesStore;
import com.weizilla.transit.groups.Group;
import com.weizilla.transit.groups.GroupsStore;
import com.weizilla.transit.source.DataSource;

import java.util.Collection;
import java.util.List;

public class BusController
{
    private final DataSource dataSource;
    private final FavoritesStore favoritesStore;
    private final GroupsStore groupsStore;

    public BusController(DataSource dataSource, FavoritesStore favoritesStore, GroupsStore groupsStore)
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

    public Collection<Prediction> getPredictions(List<Integer> stopIds, List<String> routeIds)
    {
        return dataSource.getPredictions(stopIds, routeIds);
    }

    public void saveFavorite(String routeId)
    {
        favoritesStore.saveFavorite(routeId);
    }

    public Collection<String> getFavoriteRouteIds()
    {
        return favoritesStore.getRouteIds();
    }

    public void saveFavorite(int stopId, String routeId, Direction direction)
    {
        favoritesStore.saveFavorite(stopId, routeId, direction);
    }

    public Collection<Integer> getFavoriteStopIds(String route, Direction direction)
    {
        return favoritesStore.getStopIds(route, direction);
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

    public void addStopToGroup(int groupId, int stopId)
    {
        groupsStore.addToGroup(groupId, stopId);
    }

    public void removeStopFromGroup(int groupId, int stopId)
    {
        groupsStore.removeFromGroup(groupId, stopId);
    }

    public Collection<Integer> getStopIdsForGroup(int id)
    {
        return groupsStore.getStopIds(id);
    }
}
