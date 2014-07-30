package com.weizilla.transit.bus.source.stream;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Prediction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;
import com.weizilla.transit.utils.TimeConverter;
import org.joda.time.DateTime;
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

        Collection<Direction> directions = source.getDirections(new Route("22"));

        assertNotNull(directions);
        assertEquals(2, directions.size());

        List<Direction> expected = Lists.newArrayList(Direction.Northbound, Direction.Southbound);
        assertEquals(expected, directions);
    }

    @Test
    public void readsStopsFromXmlStream() throws Exception
    {
        streamProvider.setStreamFromResource("/getstops_22_N.xml");

        Collection<Stop> stops = source.getStops(new Route("22"), Direction.Northbound);

        assertNotNull(stops);
        assertEquals(86, stops.size());

        assertStop(1926, "Clark & Addison", 41.947086853598, -87.656360864639, Iterables.get(stops, 0));
    }

    @Test
    public void readsPredictionsFromXmlStream() throws Exception
    {
        streamProvider.setStreamFromResource("/getpredictions_22_1926.xml");

        DateTime generated = TimeConverter.parse("20140702 18:04");
        String stopName = "Clark & Addison";
        int stopId = 1926;
        int distanceFt = 840;
        String route = "22";
        Direction direction = Direction.Northbound;
        String destination = "Foster";
        DateTime prediction = TimeConverter.parse("20140702 18:05");
        boolean delayed = false;

        Collection<Prediction> predictions = source.getPredictions(new Route(route), new Stop(stopId));

        assertNotNull(prediction);
        assertEquals(9, predictions.size());

        Prediction actual = Iterables.get(predictions, 0);
        assertPrediction(generated, stopName, stopId, distanceFt, route, direction, destination, prediction, delayed, actual);
    }

    @Test
    public void readsDelayedPredictionFromXmlStream() throws Exception
    {
        streamProvider.setStreamFromResource("/getpredictions_22_1926_delay.xml");

        DateTime generated = TimeConverter.parse("20140702 18:04");
        String stopName = "Clark & Addison";
        int stopId = 1926;
        int distanceFt = 840;
        String route = "22";
        Direction direction = Direction.Northbound;
        String destination = "Foster";
        DateTime prediction = TimeConverter.parse("20140702 18:05");
        boolean delayed = true;

        Collection<Prediction> predictions = source.getPredictions(new Route(route), new Stop(stopId));

        assertNotNull(prediction);
        assertEquals(1, predictions.size());

        Prediction actual = Iterables.get(predictions, 0);
        assertPrediction(generated, stopName, stopId, distanceFt, route, direction, destination, prediction, delayed, actual);
    }

    private static void assertStop(int stopId, String stopName, double lat, double lon, Stop actual)
    {
        double delta = 0.01;
        assertEquals(stopId, actual.getId());
        assertEquals(stopName, actual.getName());
        assertEquals(lat, actual.getLatitude(), delta);
        assertEquals(lon, actual.getLongitude(), delta);
    }

    private static void assertPrediction(DateTime generated, String stopName, int stopId, int distanceFt,
                                         String route, Direction routeDirection, String destination,
                                         DateTime prediction, boolean delayed, Prediction actual)
    {
        assertEquals(generated, actual.getGenerated());
        assertEquals(stopName, actual.getStopName());
        assertEquals(stopId, actual.getStopId());
        assertEquals(distanceFt, actual.getDistanceFt());
        assertEquals(route, actual.getRoute());
        assertEquals(routeDirection, actual.getRouteDirection());
        assertEquals(destination, actual.getDestination());
        assertEquals(prediction, actual.getPrediction());
        assertEquals(delayed, actual.isDelayed());
    }

    @Test
    public void returnsEmptyIfNoStreamError() throws Exception
    {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.OFF);

        assertTrue(source.getRoutes().isEmpty());
        assertTrue(source.getDirections(new Route("22")).isEmpty());
    }
}
