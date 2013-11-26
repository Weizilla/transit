package com.weizilla.transit.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.TextView;
import com.google.common.collect.Lists;
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
    //reference data from transit-unit project, routes.xml in resources
    private static final Route ROUTE_1 = new Route("1234", "XXXBronzeville/Union Station", false);
    private static final Route ROUTE_2 = new Route("222", "YYYHyde Park Express", false);
    private static final Route ROUTE_2_FAV = new Route("222", "YYYHyde Park Express", true);
    private static final Route ROUTE_3 = new Route("15", "Jeffery Local", false);
    private static final int TIMEOUT = 1000;

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

        // one for favorites, one for retrieved
        solo.waitForView(R.id.uiBusRouteList);
        solo.waitForView(R.id.uiBusRouteList);
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
        //TODO test  more routes except clicking on first item exits application
        List<Route> expectedRoutes = Lists.newArrayList(
                ROUTE_1
//                new Route("222", "YYYHyde Park Express")
        );

        for (int i = 0; i < expectedRoutes.size(); i++)
        {
            Route expected = expectedRoutes.get(i);
            Route actual = clickInList(i + 1);
            assertEquals(expected, actual);
        }
    }

    public void testFavoriteRoutesPopulatedByDB()
    {
        addRouteToFavDb(ROUTE_2);

        activity.refreshFavorites();
        solo.waitForView(R.id.uiBusRouteList);

        Route actual = clickInList(1);
        assertEquals(ROUTE_2_FAV, actual);
    }

    public void testContextMenuAddsSecondRowToFavorite()
    {
        Route fav = clickLongInList(2);
        assertEquals(ROUTE_2, fav);

        solo.waitForDialogToOpen(TIMEOUT);
        // add route to favorite in dialog
        solo.clickInList(1);

        solo.waitForView(R.id.uiBusRouteList);

        Route actual = clickInList(1);
        assertEquals(ROUTE_2_FAV, actual);
    }

    public void testContextMenuRemovesFavorite()
    {
        addRouteToFavDb(ROUTE_2);

        activity.refreshFavorites();
        solo.waitForView(R.id.uiBusRouteList);

        Route fav = clickLongInList(1);
        assertEquals(ROUTE_2_FAV, fav);

        solo.waitForDialogToOpen(TIMEOUT);
        // remove route from favorite in dialog
        solo.clickInList(1);

        solo.waitForView(R.id.uiBusRouteList);

        Route actual = clickInList(1);
        assertEquals(ROUTE_1, actual);
    }

    public void testFilteredDropdown()
    {
        EditText edit = solo.getEditText(0);
        solo.typeText(edit, ROUTE_3.getName());

        Route actual = clickInList(1);
        assertEquals(ROUTE_3, actual);
    }

    private void addRouteToFavDb(Route route)
    {
        FavRouteStore favRouteStore = new FavRouteStore(activity);
        favRouteStore.open();
        favRouteStore.addRoute(route);
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

    public static Route parseRoute(List<TextView> views)
    {
        TextView isFavView = views.get(0);
        String id = views.get(1).getText().toString();
        String name = views.get(2).getText().toString();
        int backgroundColor = ((ColorDrawable) isFavView.getBackground()).getColor();
        boolean isFav = backgroundColor == BusRouteSelector.FAV_BACKGROUND_COLOR;
        return new Route(id, name, isFav);
    }
}
