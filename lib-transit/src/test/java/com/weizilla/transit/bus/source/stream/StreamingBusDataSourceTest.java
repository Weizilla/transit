package com.weizilla.transit.bus.source.stream;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.common.collect.Lists;
import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Route;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class StreamingBusDataSourceTest
{
    private BusInputStreamProviderStub streamProvider;
    private StreamingBusDataSource source;

    @Before
    public void setUp() throws Exception
    {
        streamProvider = new BusInputStreamProviderStub();
        source = new StreamingBusDataSource(streamProvider);
    }

    @After
    public void tearDown() throws Exception
    {
        streamProvider.closeStream();
    }

    @Test
    public void readsRoutesFromXmlStream() throws Exception
    {
        streamProvider.setStreamFromResource("/getroutes.xml");

        Collection<Route> routes = source.getRoutes();
        assertNotNull(routes);
        assertEquals(127, routes.size());

        Route first = routes.iterator().next();
        assertEquals("1", first.getId());
        assertEquals("Bronzeville/Union Station", first.getName());

        streamProvider.closeStream();
    }

    @Test
    public void readsDirectionsFromXmlStream() throws Exception
    {
        streamProvider.setStreamFromResource("/getdirections_22.xml");

        String route = "22";
        Collection<Direction> directions = source.getDirections(route);

        assertNotNull(directions);
        assertEquals(2, directions.size());

        List<Direction> expected = Lists.newArrayList(Direction.Northbound, Direction.Southbound);
        assertEquals(expected, directions);
    }

    @Test
    public void returnsEmptyIfNoStreamError() throws Exception
    {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.OFF);

        assertTrue(source.getRoutes().isEmpty());
        assertTrue(source.getDirections("22").isEmpty());
    }
}
