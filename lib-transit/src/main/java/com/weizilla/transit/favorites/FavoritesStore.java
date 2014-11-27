package com.weizilla.transit.favorites;

import java.util.Collection;

public interface FavoritesStore
{
    void saveRoute(String id);
    Collection<String> getRouteIds();
    void saveStop(int id);
    Collection<Integer> getStopIds();
}
