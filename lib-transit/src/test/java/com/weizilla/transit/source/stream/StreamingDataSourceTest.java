package com.weizilla.transit.source.stream;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Prediction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.source.DataSourceException;
import com.weizilla.transit.utils.TimeConverter;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class StreamingDataSourceTest
{
    private static final Direction DIRECTION = Direction.Northbound;
    private static final String ROUTE_ID = "22";
    private static final int STOP_ID = 1926;
    private InputStreamProviderStub streamProvider;
    private StreamingDataSource source;

    @Before
    public void setUp() throws Exception
    {
        streamProvider = new InputStreamProviderStub();
        source = new StreamingDataSource(streamProvider);
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
    }

    @Test
    public void readsDirectionsFromXmlStream() throws Exception
    {
        streamProvider.setStreamFromResource("/getdirections_22.xml");

        Collection<Direction> directions = source.getDirections(ROUTE_ID);

        assertNotNull(directions);
        assertEquals(2, directions.size());

        List<Direction> expected = Lists.newArrayList(Direction.Northbound, Direction.Southbound);
        assertEquals(expected, directions);
    }

    @Test
    public void readsStopsFromXmlStream() throws Exception
    {
        streamProvider.setStreamFromResource("/getstops_22_N.xml");

        Collection<Stop> stops = source.getStops(ROUTE_ID, DIRECTION);

        assertNotNull(stops);
        assertEquals(86, stops.size());

        Stop actual = Iterables.get(stops, 0);
        assertStop("Clark & Addison", 41.947, -87.656, actual);
    }

    @Test
    public void readsPredictionsFromXmlStream() throws Exception
    {
        streamProvider.setStreamFromResource("/getpredictions_22_1926.xml");

        DateTime generated = TimeConverter.parse("20140702 18:04");
        String stopName = "Clark & Addison";
        int distanceFt = 840;
        String destination = "Foster";
        DateTime prediction = TimeConverter.parse("20140702 18:05");
        boolean delayed = false;

        Collection<Prediction> predictions = source.getPredictions(ROUTE_ID, STOP_ID);

        assertNotNull(prediction);
        assertEquals(9, predictions.size());

        Prediction actual = Iterables.get(predictions, 0);
        assertPrediction(generated, stopName, distanceFt, destination, prediction, delayed, actual);
    }

    @Test
    public void readsDelayedPredictionFromXmlStream() throws Exception
    {
        streamProvider.setStreamFromResource("/getpredictions_22_1926_delay.xml");

        DateTime generated = TimeConverter.parse("20140702 18:04");
        String stopName = "Clark & Addison";
        int distanceFt = 840;
        String destination = "Foster";
        DateTime prediction = TimeConverter.parse("20140702 18:05");
        boolean delayed = true;

        Collection<Prediction> predictions = source.getPredictions(ROUTE_ID, STOP_ID);

        assertNotNull(prediction);
        assertEquals(1, predictions.size());

        Prediction actual = Iterables.get(predictions, 0);
        assertPrediction(generated, stopName, distanceFt, destination, prediction, delayed, actual);
    }

    @Test
    public void throwsExceptionIfGetRoutesHasError() throws Exception
    {
        String errorMsg = "ERROR MESSAGE";
        streamProvider.setStreamFromResource("/error.xml");
        try
        {
            source.getRoutes();
            fail("Should have thrown error");
        }
        catch (DataSourceException e)
        {
            assertEquals(errorMsg, e.getMessage());
        }
    }

    @Test
    public void throwsExceptionIfGetDirectionsHasError() throws Exception
    {
        String errorMsg = "ERROR MESSAGE";
        streamProvider.setStreamFromResource("/error.xml");
        try
        {
            source.getDirections(ROUTE_ID);
            fail("Should have thrown error");
        }
        catch (DataSourceException e)
        {
            assertEquals(errorMsg, e.getMessage());
        }
    }

    @Test
    public void throwsExceptionIfGetPredictionsHasError() throws Exception
    {
        String errorMsg = "ERROR MESSAGE";
        streamProvider.setStreamFromResource("/error.xml");
        try
        {
            source.getPredictions(ROUTE_ID, STOP_ID);
            fail("Should have thrown error");
        }
        catch (DataSourceException e)
        {
            assertEquals(errorMsg, e.getMessage());
        }
    }

    @Test
    public void throwsExceptionIfGetStopsHasError() throws Exception
    {
        String errorMsg = "ERROR MESSAGE";
        streamProvider.setStreamFromResource("/error.xml");
        try
        {
            source.getStops(ROUTE_ID, DIRECTION);
            fail("Should have thrown error");
        }
        catch (DataSourceException e)
        {
            assertEquals(errorMsg, e.getMessage());
        }
    }

    private static void assertStop(String stopName, double lat, double lon, Stop actual)
    {
        double delta = 0.01;
        assertEquals(STOP_ID, actual.getId());
        assertEquals(stopName, actual.getName());
        assertEquals(lat, actual.getLatitude(), delta);
        assertEquals(lon, actual.getLongitude(), delta);
        assertEquals(ROUTE_ID, actual.getRouteId());
        assertEquals(DIRECTION, actual.getDirection());
    }

    private static void assertPrediction(DateTime generated, String stopName, int distanceFt,
                                         String destination, DateTime prediction,
        boolean delayed, Prediction actual)
    {
        assertEquals(generated, actual.getGenerated());
        assertEquals(stopName, actual.getStopName());
        assertEquals(STOP_ID, actual.getStopId());
        assertEquals(distanceFt, actual.getDistanceFt());
        assertEquals(ROUTE_ID, actual.getRoute());
        assertEquals(DIRECTION, actual.getRouteDirection());
        assertEquals(destination, actual.getDestination());
        assertEquals(prediction, actual.getPrediction());
        assertEquals(delayed, actual.isDelayed());
    }

    @Test(expected = DataSourceException.class)
    public void throwsExceptionIfNoStream() throws Exception
    {
        source.getRoutes();
    }
}
