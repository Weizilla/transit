package com.weizilla.transit.data;

import com.google.common.collect.Lists;
import junit.framework.TestCase;

import java.util.Collections;
import java.util.List;

/**
 * tests the route sorting algorithm
 *
 * @author wei
 *         Date: 11/22/13
 *         Time: 2:10 PM
 */
public class RouteSortTest extends TestCase
{
    public void testSortWithId()
    {
        Route a = new Route("a", null, false);
        Route b = new Route("b", "a", false);
        List<Route> routes = Lists.newArrayList(b, a);
        List<Route> expected = Lists.newArrayList(a, b);
        Collections.sort(routes);
        assertEquals(expected, routes);
    }

    public void testSortWithOneNullId()
    {
        Route a = new Route(null, null, false);
        Route b = new Route("b", null, false);
        List<Route> routes = Lists.newArrayList(b, a);
        List<Route> expected = Lists.newArrayList(a, b);
        Collections.sort(routes);
        assertEquals(expected, routes);
    }

    public void testSortWithName()
    {
        Route a = new Route(null, "a", false);
        Route b = new Route(null, "b", false);
        List<Route> routes = Lists.newArrayList(b, a);
        List<Route> expected = Lists.newArrayList(a, b);
        Collections.sort(routes);
        assertEquals(expected, routes);
    }

    public void testNoErrorsWithAllNull()
    {
        Route a = new Route(null, null, false);
        Route b = new Route(null, null, false);
        List<Route> routes = Lists.newArrayList(b, a);
        Collections.sort(routes);
    }

    public void testSortWithFavorite()
    {
        Route a = new Route(null, "a", true);
        Route b = new Route(null, "a", false);
        List<Route> routes = Lists.newArrayList(b, a);
        List<Route> expected = Lists.newArrayList(a, b);
        Collections.sort(routes);
        assertEquals(expected, routes);
    }
}
