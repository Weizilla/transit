package com.weizilla.transit.ui;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import com.jayway.android.robotium.solo.Solo;
import com.weizilla.transit.R;
import com.weizilla.transit.activity.BusRouteSelector;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.dataproviders.MockTransitDataProvider;
import com.weizilla.transit.dataproviders.TransitDataProvider;
import com.weizilla.transit.db.FavRouteStore;

import java.util.List;

/**
 * tests the ui for the bus route selector
 *
 * @author wei
 *         Date: 9/2/13
 *         Time: 6:02 PM
 */
public class BusRouteSelectorUITest extends ActivityInstrumentationTestCase2<BusRouteSelector>
{
    private BusRouteSelector activity;
    private Solo solo;
    private FavRouteStore favRouteStore;

    public BusRouteSelectorUITest()
    {
        super(BusRouteSelector.class);
    }

    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        Intent intent = new Intent();
        intent.putExtra(TransitDataProvider.KEY, new MockTransitDataProvider());
        this.setActivityIntent(intent);

        activity = getActivity();
        solo = new Solo(getInstrumentation(), activity);
        favRouteStore = new FavRouteStore(activity);
    }

    @Override
    public void tearDown() throws Exception
    {
        solo.finishOpenedActivities();
        favRouteStore.deleteDb();
        super.tearDown();
    }

    public void testStartPopulatesList()
    {
        //TODO test  more routes
        String[][] expected =
        {
            {"1234", "XXXBronzeville/Union Station"}
        };

        // one for favorites, one for retrieved
        solo.waitForView(R.id.uiBusRouteList);
        solo.waitForView(R.id.uiBusRouteList);

        solo.scrollToTop();
        for (int i = 0; i < expected.length; i++)
        {
            List<TextView> itemViews = solo.clickInList (i + 1, 0);
            TextView idView = itemViews.get(0);
            TextView nameView = itemViews.get(1);
            assertEquals(expected[i][0], idView.getText().toString());
            assertEquals(expected[i][1], nameView.getText().toString());
        }
    }

    public void testFavoriteRoutesPopulatedByDB()
    {
        FavRouteStore favRouteStore = new FavRouteStore(activity);
        favRouteStore.open();
        favRouteStore.addRoute(new Route("TEST_ID", "TEST_NAME"));

        activity.refreshFavorites();

        // one for favorites, one for retrieved
        solo.waitForView(R.id.uiBusRouteList);
        solo.waitForView(R.id.uiBusRouteList);

        solo.scrollToTop();
        List<TextView> itemViews = solo.clickInList(1, 0);
        TextView idView = itemViews.get(0);
        TextView nameView = itemViews.get(1);
        assertEquals("TEST_ID", idView.getText().toString());
        assertEquals("TEST_NAME", nameView.getText().toString());
    }
}
