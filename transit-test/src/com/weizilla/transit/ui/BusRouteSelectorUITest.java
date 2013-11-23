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
        //reference data from transit-unit project, routes.xml in resources
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
            Route actual = clickInList(i + 1);
            String expectedId = expected[i][0];
            String expectedName = expected[i][1];
            assertEquals(expectedId, actual.getId());
            assertEquals(expectedName, actual.getName());
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
        Route actual = clickInList(1);
        assertEquals("TEST_ID", actual.getId());
        assertEquals("TEST_NAME", actual.getName());
    }

    public void testContextMenuAddsSecondRowToFavorite()
    {
        //reference data from transit-unit project, routes.xml in resources
        String expectedId = "222";
        String expectedName = "YYYHyde Park Express";

        // one for favorites, one for retrieved
        solo.waitForView(R.id.uiBusRouteList);
        solo.waitForView(R.id.uiBusRouteList);

        Route fav = clickLongInList(2);
        assertEquals(expectedId, fav.getId());
        assertEquals(expectedName, fav.getName());

        solo.waitForDialogToOpen(1000);
        // add route to favorite in dialog
        solo.clickInList(1);

        activity.refreshFavorites();
        solo.waitForView(R.id.uiBusRouteList);

        Route actual = clickInList(1);
        assertEquals(expectedId, actual.getId());
        assertEquals(expectedName, actual.getName());
    }

    private Route clickInList(int line)
    {
        solo.scrollToTop();
        List<TextView> views = solo.clickInList(line, 0);
        return parseRoute(views);
    }

    private Route clickLongInList(int line)
    {
        solo.scrollToTop();
        List<TextView> views = solo.clickLongInList(line, 0);
        return parseRoute(views);
    }

    private static Route parseRoute(List<TextView> views)
    {
        String id = views.get(0).getText().toString();
        String name = views.get(1).getText().toString();
        return new Route(id, name);
    }
}
