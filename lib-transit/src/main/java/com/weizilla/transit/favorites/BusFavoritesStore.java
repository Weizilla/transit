package com.weizilla.transit.favorites;

import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;

import java.util.Collection;

public interface BusFavoritesStore
{
    void saveFavorite(Route route);
    Collection<String> getFavoriteRoutes();
    void saveFavorite(Stop stop);
    Collection<Integer> getFavoriteStops(String route, Direction direction);
}
