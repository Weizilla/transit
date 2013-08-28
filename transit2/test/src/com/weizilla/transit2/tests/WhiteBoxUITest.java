package com.weizilla.transit2.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.jayway.android.robotium.solo.Solo;
import com.weizilla.transit2.R;
import com.weizilla.transit2.activity.BusStopPrediction;

import java.util.List;

public class WhiteBoxUITest extends ActivityInstrumentationTestCase2<BusStopPrediction>
{
    private static final String BUS_STOP_ID = "BUS STOP ID TEST";
    private Solo solo;

    public WhiteBoxUITest() {
        super(BusStopPrediction.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testGetPredictionsPopulatesList()
    {
        EditText busStopIdInput = (EditText) solo.getView(R.id.uiBusStopIDInput);
        solo.clearEditText(busStopIdInput);
        solo.enterText(busStopIdInput, BUS_STOP_ID);

        Button retrievePredictionsButton = (Button) solo.getView(R.id.uiRetrievePredictions);
        solo.clickOnView(retrievePredictionsButton);

        ListView predictionsDisplay = solo.getCurrentViews(ListView.class).get(0);
        List<TextView> predictions = solo.getCurrentViews(TextView.class, predictionsDisplay);

        assertEquals(1, predictions.size());
        assertEquals(BUS_STOP_ID, predictions.get(0).getText().toString());

    }
}