package com.weizilla.transit;

import com.google.common.collect.Iterables;
import com.weizilla.transit.cache.CacheStore;
import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Prediction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.favorites.FavoritesStore;
import com.weizilla.transit.groups.Group;
import com.weizilla.transit.groups.GroupsStore;
import com.weizilla.transit.source.DataSource;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BusController
{
    private final DataSource dataSource;
    private final FavoritesStore favoritesStore;
    private final GroupsStore groupsStore;
    private final CacheStore cacheStore;

    public BusController(DataSource dataSource, FavoritesStore favoritesStore,
        GroupsStore groupsStore, CacheStore cacheStore)
    {
        this.dataSource = dataSource;
        this.favoritesStore = favoritesStore;
        this.groupsStore = groupsStore;
        this.cacheStore = cacheStore;
    }

    public Collection<Route> getRoutes()
    {
        Collection<Route> routes = dataSource.getRoutes();
        if (cacheStore != null)
        {
            cacheStore.updateRoutes(routes);
        }
        return routes;
    }

    public Collection<Direction> getDirections(String routeId)
    {
        return dataSource.getDirections(routeId);
    }

    public Collection<Stop> getStops(String routeId, Direction direction)
    {
        Collection<Stop> stops = dataSource.getStops(routeId, direction);
        if (cacheStore != null)
        {
            cacheStore.updateStops(stops);
        }
        return stops;
    }

    public Collection<Prediction> getPredictions(List<Integer> stopIds, List<String> routeIds)
    {
        return dataSource.getPredictions(stopIds, routeIds);
    }

    public void saveFavoriteRoute(String id)
    {
        favoritesStore.saveRoute(id);
    }

    public Collection<Route> getFavoriteRoutes()
    {
        Collection<String> routeIds = favoritesStore.getRouteIds();
        return cacheStore.getRoutes(routeIds);
    }

    public void saveFavoriteStop(int id)
    {
        favoritesStore.saveStop(id);
    }

    public Collection<Stop> getFavoriteStops()
    {
        Collection<Integer> stopIds = favoritesStore.getStopIds();
        return cacheStore.getStops(stopIds);
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

    public Collection<Stop> getStopsForGroup(int id)
    {
        Collection<Integer> stopIds = groupsStore.getStopIds(id);
        return cacheStore.getStops(stopIds);
    }

    public Route lookupRoute(String id)
    {
        return Iterables.getFirst(cacheStore.getRoutes(Collections.singletonList(id)), null);
    }

    public Collection<Route> lookupRoutes(Collection<String> routeIds)
    {
        return cacheStore.getRoutes(routeIds);
    }

    public Collection<Stop> lookupStops(Collection<Integer> stopIds)
    {
        return cacheStore.getStops(stopIds);
    }
}
