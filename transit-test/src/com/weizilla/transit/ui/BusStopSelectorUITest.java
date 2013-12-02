package com.weizilla.transit.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.TextView;
import com.google.common.collect.Lists;
import com.jayway.android.robotium.solo.Solo;
import com.weizilla.transit.R;
import com.weizilla.transit.activity.BusStopSelector;
import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.dataproviders.MockTransitDataProvider;
import com.weizilla.transit.dataproviders.TransitDataProvider;
import com.weizilla.transit.db.FavStopStore;

import java.util.List;

/**
 * Tests the bus stop selector activity and ui
 *
 * @author wei
 *         Date: 9/3/13
 *         Time: 9:43 PM
 */
public class BusStopSelectorUITest extends ActivityInstrumentationTestCase2<BusStopSelector>
{
    //reference data from transit-unit project, routes.xml in resources
    private static final Stop STOP_1 = new Stop(1926, "XXXClark & Addison", false);
    private static final Stop STOP_2 = new Stop(1939, "Clark & Ainslie", false);
    private static final Stop STOP_2_FAV = new Stop(1939, "Clark & Ainslie", true);
    private static final Stop STOP_3 = new Stop(1958, "Clark & Albion", false);
    private static final Route TEST_ROUTE = new Route("TEST_ROUTE_ID", "TEST_ROUTE_NAME", false);
    private static final Direction TEST_DIR = Direction.Eastbound;
    private static final int TIMEOUT = 1000;

    private BusStopSelector activity;
    private Solo solo;
    private FavStopStore favStopStore;

    public BusStopSelectorUITest()
    {
        super(BusStopSelector.class);
    }

    //TODO test clicking on item returns proper stop obj

    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        Intent intent = new Intent();
        intent.putExtra(TransitDataProvider.KEY, new MockTransitDataProvider());
        intent.putExtra(Route.KEY, TEST_ROUTE);
        intent.putExtra(Direction.KEY, (Parcelable) TEST_DIR);
        setActivityIntent(intent);

        activity = getActivity();
        solo = new Solo(getInstrumentation(), activity);
        favStopStore = new FavStopStore(activity);

        // one for favorites, one for retrieve
        solo.waitForView(R.id.uiBusStopList);
        solo.waitForView(R.id.uiBusStopList);
    }

    @Override
    public void tearDown() throws Exception
    {
        solo.finishOpenedActivities();
        favStopStore.deleteDb();
        super.tearDown();
    }

    public void testStartPopulatesList()
    {
        //TODO add more and get working with click handler
        List<Stop> expectedStops = Lists.newArrayList(
                STOP_1
        );

        for (int i = 0; i < expectedStops.size(); i++)
        {
            Stop expected = expectedStops.get(i);
            Stop actual = clickInList(i + 1);
            assertEqualsUi(expected, actual);
        }
    }

    public void testFavoriteRoutesPopulatedByDB()
    {
        addStopToFavDb(TEST_ROUTE, TEST_DIR, STOP_2);

        activity.refreshFavorites();
        solo.waitForView(R.id.uiBusStopList);

        Stop actual = clickInList(1);
        assertEqualsUi(STOP_2_FAV, actual);
    }

    private void addStopToFavDb(Route route, Direction direction, Stop stop)
    {
        FavStopStore favStopStore = new FavStopStore(activity);
        favStopStore.open();
        favStopStore.addStop(route, direction, stop);
    }

    public void testContextMenuAddsSecondRowToFavorite()
    {
        Stop fav = clickLongInList(2);
        assertEqualsUi(STOP_2, fav);

        solo.waitForDialogToOpen(TIMEOUT);
        // add stop to favorite in dialog
        solo.clickInList(1);

        solo.waitForView(R.id.uiBusStopList);

        Stop stop = clickInList(1);
        assertEqualsUi(STOP_2_FAV, stop);
    }

    public void testContextMenuRemovesFavorite()
    {
        addStopToFavDb(TEST_ROUTE, TEST_DIR, STOP_2);

        activity.refreshFavorites();
        solo.waitForView(R.id.uiBusStopList);

        Stop fav = clickLongInList(1);
        assertEqualsUi(STOP_2_FAV, fav);

        solo.waitForDialogToOpen(TIMEOUT);
        // remove route from favorite in dialog
        solo.clickInList(1);

        solo.waitForView(R.id.uiBusStopList);

        Stop actual = clickInList(1);
        assertEqualsUi(STOP_1, actual);
    }

    public void testFilteredDropdown()
    {
        EditText edit = solo.getEditText(0);
        solo.typeText(edit, STOP_3.getName());

        Stop actual = clickInList(1);
        assertEqualsUi(STOP_3, actual);
    }

    private Stop clickInList(int line)
    {
        solo.scrollToTop();
        List<TextView> views = solo.clickInList(line, 0);
        return parseStop(views);
    }

    private Stop clickLongInList(int line)
    {
        solo.scrollToTop();
        List<TextView> views = solo.clickLongInList(line, 0);
        return parseStop(views);
    }

    public static Stop parseStop(List<TextView> views)
    {
        TextView isFavView = views.get(0);
        String name = views.get(1).getText().toString();
        int backgroundColor = ((ColorDrawable) isFavView.getBackground()).getColor();
        boolean isFav = backgroundColor == BusStopSelector.FAV_BACKGROUND_COLOR;
        return new Stop(0, name, isFav);
    }

    private static void assertEqualsUi(Stop expected, Stop actual)
    {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.isFavorite(), actual.isFavorite());
    }
}
