package com.weizilla.transit.ui;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;
import com.jayway.android.robotium.solo.Solo;
import com.weizilla.transit.R;
import com.weizilla.transit.activity.BusStopPicker;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.dataproviders.MockTransitDataProvider;
import com.weizilla.transit.dataproviders.TransitDataProvider;
import com.weizilla.transit.db.FavRouteStore;
import com.weizilla.transit.db.FavStopStore;

import java.util.List;

/**
 * tests the end to end of picking a bus stop and
 * seeing the predictions come up
 *
 * @author wei
 *         Date: 11/25/13
 *         Time: 4:38 PM
 */
public class BusStopPickerTest extends ActivityInstrumentationTestCase2<BusStopPicker>
{
    private static final Stop TEST_STOP = new Stop(0, "Clark & Addison", false);
    private static final Route TEST_ROUTE = new Route("1234", "XXXBronzeville/Union Station", false);
    private static final String TEST_PRED_ROUTE_ID = "36";
    private static final String TEST_PRED_DEST = "Devon/Clark";

    private BusStopPicker activity;
    private Solo solo;

    public BusStopPickerTest()
    {
        super(BusStopPicker.class);
    }

    @Override
    public void setUp() throws Exception
    {
        super.setUp();


        Intent intent = new Intent();
        intent.putExtra(TransitDataProvider.KEY, new MockTransitDataProvider());
        setActivityIntent(intent);

        activity = getActivity();
        solo = new Solo(getInstrumentation(), activity);

        new FavStopStore(activity).deleteDb();
        new FavRouteStore(activity).deleteDb();
    }

    @Override
    public void tearDown() throws Exception
    {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testPickStopsAndGetPredictions()
    {
        // test route
        solo.waitForView(R.id.uiBusRouteList);
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
        solo.scrollToTop();
        List<TextView> stopViews = solo.clickInList(1, 0);
        Stop actualStop = BusStopSelectorUITest.parseStop(stopViews);
        assertEquals(TEST_STOP, actualStop);

        // test prediction
        solo.waitForView(R.id.uiBusPredictionDisplay);
        solo.scrollToTop();
        List<TextView> predViews = solo.clickInList(1, 0);

        String predRouteId = predViews.get(0).getText().toString();
        assertEquals(predRouteId, TEST_PRED_ROUTE_ID);

        String predRouteDest = predViews.get(1).getText().toString();
        assertEquals(predRouteDest, TEST_PRED_DEST);

        // no way to set ref time for testing predicion
        // so just assert is not zero
        String predEtaStr = predViews.get(2).getText().toString();
        long predEta = Integer.parseInt(predEtaStr);
        assertTrue(predEta != 0);

    }
}
