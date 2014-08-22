package com.weizilla.transit.favorites;

import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;

import java.util.Collection;

public class SqliteFavoritesStore implements BusFavoritesStore
{
    @Override
    public void saveFavorite(Route route)
    {
        //TODO Auto-generated

    }

    @Override
    public Collection<String> getFavoriteRoutes()
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
    public Collection<Integer> getFavoriteStops(String route, Direction direction)
    {
        //TODO Auto-generated
        return null;
    }
}
