package com.weizilla.transit.favorites;

import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;

import java.util.Collection;

public interface BusFavoritesStore
{
    void saveFavorite(Route route);
    Collection<String> getFavoriteRouteIds();
    void saveFavorite(Stop stop);
    Collection<Integer> getFavoriteStopIds(String route, Direction direction);
}
