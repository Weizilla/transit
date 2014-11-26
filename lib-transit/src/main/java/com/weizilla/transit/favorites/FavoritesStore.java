package com.weizilla.transit.favorites;

import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;

import java.util.Collection;

public interface FavoritesStore
{
    void saveFavorite(Route route);
    Collection<String> getFavoriteRouteIds();
    void saveFavorite(Stop stop);
    Collection<Integer> getFavoriteStopIds(String route, Direction direction);
}
