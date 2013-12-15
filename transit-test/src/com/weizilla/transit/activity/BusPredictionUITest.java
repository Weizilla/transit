package com.weizilla.transit.activity;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jayway.android.robotium.solo.Solo;
import com.weizilla.transit.R;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.dataproviders.MockTransitDataProvider;
import com.weizilla.transit.dataproviders.TransitDataProvider;
import com.weizilla.transit.util.TimeConverter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BusPredictionUITest extends ActivityInstrumentationTestCase2<BusPrediction>
{
    private static final int TEST_BUS_ID = 123456;
    private static final Stop TEST_STOP = new Stop(TEST_BUS_ID, "XXXClark & Addison", false);
    private static final Route TEST_ROUTE = new Route("1234", "XXXBronzeville/Union Station", false);
    private static final String MULTIPLE_STOP_IDS = "1832 1833";
    private Solo solo;
    private BusPrediction activity;

    public BusPredictionUITest()
    {
        super(BusPrediction.class);
    }

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        Intent intent = new Intent();
        intent.putExtra(TransitDataProvider.KEY, new MockTransitDataProvider());
        this.setActivityIntent(intent);
        activity = getActivity();
        activity.setRefTime(genRefTime());
        solo = new Solo(getInstrumentation(), activity);
    }

    @Override
    public void tearDown() throws Exception
    {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testSingleBusIdGetsPredictions()
    {
        String[][] expected =
                {
                        {"36", "Devon/Clark", "8"},
                        {"36", "Devon/Clark", "12"},
                        {"22", "Howard", "13"},
                        {"22", "Howard", "20"},
                };

        EditText busStopIdInput = (EditText) solo.getView(R.id.uiBusPredStopIdInput);
        solo.clearEditText(busStopIdInput);
        solo.enterText(busStopIdInput, String.valueOf(TEST_BUS_ID));

        Button retrievePredictionsButton = (Button) solo.getView(R.id.uiBusPredRetrievePred);
        solo.clickOnView(retrievePredictionsButton);

        solo.waitForView(R.id.uiBusPredDisplay);

        solo.scrollListToTop(0);
        for (int i = 0; i < expected.length; i++)
        {
            ArrayList<TextView> views = solo.clickInList(i + 1, 0);
            for (int j = 0; j < expected[i].length; j++)
            {
                String expectedStr = expected[i][j];
                TextView textView = views.get(j);
                solo.waitForView(textView);
                assertEquals(expectedStr, textView.getText().toString());
            }
        }

    }

    public void testMultipleBusIdsGetsPredictions()
    {
        String[][] expected =
        {
            {"36", "LaSalle Metra Station", "10"},
            {"22", "Harrison", "11"},
            {"22", "Harrison", "32"},
            {"36", "LaSalle Metra Station", "41"},
        };

        EditText busStopIdInput = (EditText) solo.getView(R.id.uiBusPredStopIdInput);
        solo.clearEditText(busStopIdInput);
        solo.enterText(busStopIdInput, MULTIPLE_STOP_IDS);

        Button retrievePredictionsButton = (Button) solo.getView(R.id.uiBusPredRetrievePred);
        solo.clickOnView(retrievePredictionsButton);

        solo.waitForView(R.id.uiBusPredDisplay);

        solo.scrollListToTop(0);
        for (int i = 0; i < expected.length; i++)
        {
            ArrayList<TextView> views = solo.clickInList(i + 1, 0);
            for (int j = 0; j < expected[i].length; j++)
            {
                String expectedStr = expected[i][j];
                TextView textView = views.get(j);
                solo.waitForView(textView);
                assertEquals(expectedStr, textView.getText().toString());
            }
        }
    }

    public void testGetStopIdAndPrediction()
    {
        String lookUpStop = solo.getString(R.string.look_up_stop);
        solo.waitForText(lookUpStop);
        solo.clickOnButton(lookUpStop);

        // test route
        solo.waitForView(R.id.uiBusRouteList);
        solo.waitForView(R.id.uiBusRouteList);
        solo.scrollToTop();
        List<TextView> routeViews = solo.clickInList(1, 0);
        Route actualRoute = BusRouteSelectorUITest.parseRoute(routeViews);
        assertEquals(TEST_ROUTE, actualRoute);

        // test direction
        Button eastButton = solo.getButton("E");
        assertTrue(eastButton.isEnabled());
        solo.clickOnButton("E");

        // test stop
        solo.waitForView(R.id.uiBusStopList);
        solo.waitForView(R.id.uiBusStopList);
        solo.waitForView(R.id.uiBusStopList);
        solo.scrollToTop();
        List<TextView> stopViews = solo.clickInList(1, 0);
        Stop actualStop = BusStopSelectorUITest.parseStop(stopViews);
        assertEquals(TEST_STOP.getName(), actualStop.getName());

        // test prediction
        String[][] expected =
        {
            {"36", "Devon/Clark", "8"},
            {"36", "Devon/Clark", "12"},
            {"22", "Howard", "13"},
            {"22", "Howard", "20"},
        };

        solo.waitForView(R.id.uiBusPredDisplay);

        solo.scrollListToTop(0);
        for (int i = 0; i < expected.length; i++)
        {
            ArrayList<TextView> views = solo.clickInList(i + 1, 0);
            for (int j = 0; j < expected[i].length; j++)
            {
                String expectedStr = expected[i][j];
                TextView textView = views.get(j);
                solo.waitForView(textView);
                assertEquals(expectedStr, textView.getText().toString());
            }
        }
    }

    public static Date genRefTime() throws ParseException
    {
        return TimeConverter.parse("20130818 17:23");
    }
}