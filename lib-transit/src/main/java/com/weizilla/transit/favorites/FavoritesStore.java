package com.weizilla.transit.favorites;

import com.weizilla.transit.data.Direction;

import java.util.Collection;

public interface FavoritesStore
{
    void saveFavorite(String routeId);
    Collection<String> getRouteIds();
    void saveFavorite(int stopId, String routeId, Direction direction);
    Collection<Integer> getStopIds(String routeId, Direction direction);
}
