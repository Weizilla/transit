package com.weizilla.transit2.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.jayway.android.robotium.solo.Solo;
import com.weizilla.transit2.R;
import com.weizilla.transit2.activity.BusStopPrediction;

public class BusPredictionUITest extends ActivityInstrumentationTestCase2<BusStopPrediction>
{
    private static final String BUS_STOP_ID = "123456";
    private Solo solo;

    public BusPredictionUITest()
    {
        super(BusStopPrediction.class);
    }

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        BusStopPrediction activity = getActivity();
        activity.setTransitDataProvider(new MockTransitDataProvider());
        solo = new Solo(getInstrumentation(), activity);
    }

    @Override
    public void tearDown() throws Exception
    {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testGetPredictionsPopulatesList()
    {
        String[] expected = new String[]
        {
            "Prediction{timestamp='20130818 17:23', type='A', stopId=1916, stopName='Clark & Schubert', vehicleId=1859, distRemaining=6114, route='36', direction='Northbound', destination='Devon/Clark', predictionTime='20130818 17:31'}",
            "Prediction{timestamp='20130818 17:23', type='A', stopId=1916, stopName='Clark & Schubert', vehicleId=1861, distRemaining=9607, route='36', direction='Northbound', destination='Devon/Clark', predictionTime='20130818 17:35'}",
            "Prediction{timestamp='20130818 17:23', type='A', stopId=1916, stopName='Clark & Schubert', vehicleId=4129, distRemaining=9473, route='22', direction='Northbound', destination='Howard', predictionTime='20130818 17:36'}",
            "Prediction{timestamp='20130818 17:23', type='A', stopId=1916, stopName='Clark & Schubert', vehicleId=4074, distRemaining=14728, route='22', direction='Northbound', destination='Howard', predictionTime='20130818 17:43'}"
        };

        EditText busStopIdInput = (EditText) solo.getView(R.id.uiBusStopIDInput);
        solo.clearEditText(busStopIdInput);
        solo.enterText(busStopIdInput, BUS_STOP_ID);

        Button retrievePredictionsButton = (Button) solo.getView(R.id.uiRetrievePredictions);
        solo.clickOnView(retrievePredictionsButton);

        solo.waitForView(R.id.uiPredictionList);

        ListView predictionsDisplay = solo.getCurrentViews(ListView.class).get(0);
        solo.scrollListToTop(0);
        for (int i = 1; i <= predictionsDisplay.getChildCount(); i++)
        {
            TextView textView = solo.clickInList(i, 0).get(0);
            solo.waitForView(textView);
            assertEquals(expected[i-1], textView.getText().toString());
        }

    }

}