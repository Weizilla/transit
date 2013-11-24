package com.weizilla.transit;

import com.weizilla.transit.data.Route;

import java.util.List;

/**
 * anything that provides a list of routes
 *
 * @author wei
 *         Date: 11/24/13
 *         Time: 5:09 PM
 */
public interface BusRoutesProvider
{
    List<Route> getRoutes();
}
