package com.weizilla.transit.tests;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import com.jayway.android.robotium.solo.Solo;
import com.weizilla.transit.R;
import com.weizilla.transit.activity.BusRouteSelector;
import com.weizilla.transit.dataproviders.TransitDataProvider;

import java.util.ArrayList;

/**
 * tests the ui for the bus route selector
 *
 * @author wei
 *         Date: 9/2/13
 *         Time: 6:02 PM
 */
public class BusRouteSelectorUITest extends ActivityInstrumentationTestCase2<BusRouteSelector>
{
    private Solo solo;

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

        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception
    {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testStartPopulatesList()
    {
        //TODO test  more routes
        String[][] expected =
        {
            {"1234", "XXXBronzeville/Union Station"}
        };

        solo.waitForView(R.id.uiBusRouteList);
        solo.scrollToTop();
        for (int i = 0; i < expected.length; i++)
        {
            ArrayList<TextView> itemViews = solo.clickInList (i + 1, 0);
            TextView idView = itemViews.get(0);
            TextView nameView = itemViews.get(1);
            assertEquals(expected[i][0], idView.getText().toString());
            assertEquals(expected[i][1], nameView.getText().toString());
        }
    }
}
