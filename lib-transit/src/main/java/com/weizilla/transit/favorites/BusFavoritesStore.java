package com.weizilla.transit.favorites;

import com.weizilla.transit.bus.data.Route;

import java.util.Collection;

public interface BusFavoritesStore
{
    void saveFavorite(Route route);
    Collection<String> getFavoriteRoutes();
}
