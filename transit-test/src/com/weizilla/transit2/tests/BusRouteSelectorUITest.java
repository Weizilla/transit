package com.weizilla.transit2.tests;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import com.jayway.android.robotium.solo.Solo;
import com.weizilla.transit2.R;
import com.weizilla.transit2.activity.BusRouteSelector;
import com.weizilla.transit2.dataproviders.TransitDataProvider;

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
        String[] expected = new String[]
        {
            "Route{id='1', name='XXXBronzeville/Union Station'}",
        };

        solo.waitForView(R.id.uiBusRouteList);
        solo.scrollToTop();
        for (int i = 0; i < expected.length; i++)
        {
            TextView textView = solo.clickInList(i + 1, 0).get(0);
            solo.waitForView(textView);
            assertEquals(expected[i], textView.getText().toString());
        }
    }
}
