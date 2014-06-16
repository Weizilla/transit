package com.weizilla.transit.favorites;

import com.weizilla.transit.bus.data.Route;

import java.util.Collection;

public interface BusFavoriteStore
{
    void saveFavorite(Route route);
    Collection<Route> getFavoriteRoutes();
}
