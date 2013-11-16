package com.weizilla.transit.tests;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import com.jayway.android.robotium.solo.Solo;
import com.weizilla.transit.R;
import com.weizilla.transit.activity.BusStopSelector;
import com.weizilla.transit.dataproviders.TransitDataProvider;

/**
 * TODO auto-generated header
 *
 * @author wei
 *         Date: 9/3/13
 *         Time: 9:43 PM
 */
public class BusStopSelectorUITest extends ActivityInstrumentationTestCase2<BusStopSelector>
{
    private Solo solo;

    public BusStopSelectorUITest()
    {
        super(BusStopSelector.class);
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
        //TODO add more and get working with click handler
        String[] expected = new String[]
        {
            "Clark & Addison",
        };

        solo.waitForView(R.id.uiBusStopList);
        solo.scrollToTop();
        for (int i = 0; i < expected.length; i++)
        {
            TextView textView = solo.clickInList(i + 1, 0).get(0);
            solo.waitForView(textView);
            assertEquals(expected[i], textView.getText().toString());
        }
    }
}
