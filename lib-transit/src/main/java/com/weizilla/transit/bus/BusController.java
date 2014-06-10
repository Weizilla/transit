package com.weizilla.transit.bus;

import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.source.BusSource;

import java.util.Collection;

public class BusController
{
    private BusSource source;

    public Collection<Route> getRoutes()
    {
        return source.getRoutes();
    }

    public void setSource(BusSource source)
    {
        this.source = source;
    }
}
