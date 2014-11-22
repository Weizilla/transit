package com.weizilla.transit.favorites.android;

import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;
import com.weizilla.transit.favorites.BusFavoritesStore;

import java.util.Collection;

public class AndroidFavoritesStore implements BusFavoritesStore
{
    @Override
    public void saveFavorite(Route route)
    {
        //TODO Auto-generated

    }

    @Override
    public Collection<String> getFavoriteRouteIds()
    {
        //TODO Auto-generated
        return null;
    }

    @Override
    public void saveFavorite(Stop stop)
    {
        //TODO Auto-generated

    }

    @Override
    public Collection<Integer> getFavoriteStopIds(String route, Direction direction)
    {
        //TODO Auto-generated
        return null;
    }
}
