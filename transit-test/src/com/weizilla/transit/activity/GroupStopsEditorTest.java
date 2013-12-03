package com.weizilla.transit.activity;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;
import com.jayway.android.robotium.solo.Solo;
import com.weizilla.transit.R;
import com.weizilla.transit.data.Group;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.dataproviders.MockTransitDataProvider;
import com.weizilla.transit.dataproviders.TransitDataProvider;
import com.weizilla.transit.db.GroupStore;

import java.text.ParseException;
import java.util.List;

/**
 * tests the group stops editor activity
 *
 * @author wei
 *         Date: 11/29/13
 *         Time: 1:47 PM
 */
public class GroupStopsEditorTest extends ActivityInstrumentationTestCase2<GroupStopsEditor>
{
    private static final long TEST_GROUP_ID = 100;
    private static final String TEST_GROUP_NAME = "TEST GROUP NAME";
    private static final Stop TEST_STOP = new Stop(123, "XXXClark & Addison", false);
    private static final Stop TEST_STOP_2 = new Stop(234, "TEST_STOP_NAME_2", false);
    private static final Route TEST_ROUTE = new Route("1234", "XXXBronzeville/Union Station", false);
    private Solo solo;
    private GroupStore store;
    private GroupStopsEditor activity;

    public GroupStopsEditorTest()
    {
        super(GroupStopsEditor.class);
    }

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
    }

    private void init()
    {
        activity = getActivity();
        solo = new Solo(getInstrumentation(), activity);
        store = new GroupStore(activity);
        store.open();
    }

    @Override
    public void tearDown() throws Exception
    {
        if (solo != null)
        {
            solo.finishOpenedActivities();
        }
        super.tearDown();
    }

    public void testGroupPopulates() throws ParseException
    {
        Intent intent = new Intent();
        intent.putExtra(TransitDataProvider.KEY, new MockTransitDataProvider());
        intent.putExtra(Group.INTENT_KEY, createTestGroup(TEST_STOP, TEST_STOP_2));
        setActivityIntent(intent);

        init();

        solo.waitForView(R.id.uiGroupName);
        TextView text = solo.getText(1);
        assertEquals(TEST_GROUP_NAME, text.getText());

        String stop1 = clickInList(1);
        assertEquals(stop1, TEST_STOP.getName());

        String stop2 = clickInList(2);
        assertEquals(stop2, TEST_STOP_2.getName());
    }

    public void testAddStop()
    {
        Group group = createTestGroup();

        Intent intent = new Intent();
        intent.putExtra(TransitDataProvider.KEY, new MockTransitDataProvider());
        intent.putExtra(Group.INTENT_KEY, group);
        setActivityIntent(intent);

        init();

        // can't init store until after activity is created but
        // we've already gave the group to the activity so create
        // it for real here
        store.createGroup(group.getId(), group.getName());

        solo.scrollToTop();
        List<TextView> views = solo.clickInList(0);
        assertTrue(views.isEmpty());

        String addStop = solo.getString(R.string.add_stop);
        solo.clickOnButton(addStop);

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
        solo.waitForView(eastButton);
        assertTrue(eastButton.isEnabled());
        solo.clickOnButton("E");

        // test stop
        solo.waitForView(R.id.uiBusStopList);
        solo.waitForView(R.id.uiBusStopList);
        solo.waitForView(R.id.uiBusStopList);
        solo.scrollToTop();
        List<TextView> stopViews = solo.clickInList(1, 0);
        solo.waitForView(stopViews.get(0));
        Stop actualStop = BusStopSelectorUITest.parseStop(stopViews);
        assertEquals(TEST_STOP.getName(), actualStop.getName());

        // back to group editor
        solo.waitForView(R.id.uiGroupStopsDisplay);
        String stopName = clickInList(1);
        assertEquals(TEST_STOP.getName(), stopName);
    }

    private String clickInList(int line)
    {
        solo.scrollToTop();
        List<TextView> views = solo.clickInList(line);
        return views.get(0).getText().toString();
    }

    private static Group createTestGroup(Stop... stops)
    {
        Group group = new Group(TEST_GROUP_ID, TEST_GROUP_NAME);
        for (Stop stop : stops)
        {
            group.addStop(stop);
        }
        return group;
    }
}
