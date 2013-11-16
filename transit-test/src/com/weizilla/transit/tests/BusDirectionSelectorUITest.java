package com.weizilla.transit.tests;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import com.jayway.android.robotium.solo.Solo;
import com.weizilla.transit.activity.BusDirectionSelector;
import com.weizilla.transit.dataproviders.TransitDataProvider;

/**
 * TODO auto-generated header
 *
 * @author wei
 *         Date: 9/3/13
 *         Time: 9:43 PM
 */
public class BusDirectionSelectorUITest extends ActivityInstrumentationTestCase2<BusDirectionSelector>
{
    private Solo solo;

    public BusDirectionSelectorUITest()
    {
        super(BusDirectionSelector.class);
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
        Button northButton = solo.getButton("N");
        assertFalse(northButton.isEnabled());

        Button eastButton = solo.getButton("E");
        assertTrue(eastButton.isEnabled());

        Button southButton = solo.getButton("S");
        assertFalse(southButton.isEnabled());

        Button westButton = solo.getButton("W");
        assertTrue(westButton.isEnabled());

    }
}
