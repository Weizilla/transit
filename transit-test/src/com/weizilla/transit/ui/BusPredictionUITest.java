package com.weizilla.transit.ui;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jayway.android.robotium.solo.Solo;
import com.weizilla.transit.R;
import com.weizilla.transit.activity.BusPrediction;
import com.weizilla.transit.dataproviders.MockTransitDataProvider;
import com.weizilla.transit.dataproviders.TransitDataProvider;
import com.weizilla.transit.util.TimeConverter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class BusPredictionUITest extends ActivityInstrumentationTestCase2<BusPrediction>
{
    private static final String BUS_STOP_ID = "123456";
    private Solo solo;
    private BusPrediction activity;

    public BusPredictionUITest()
    {
        super(BusPrediction.class);
    }

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        Intent intent = new Intent();
        intent.putExtra(TransitDataProvider.KEY, new MockTransitDataProvider());
        this.setActivityIntent(intent);
        activity = getActivity();
        activity.setRefTime(genRefTime());
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
        String[][] expected =
        {
            {"36", "Devon/Clark", "8"},
            {"36", "Devon/Clark", "12"},
            {"22", "Howard", "13"},
            {"22", "Howard", "20"},
        };

        EditText busStopIdInput = (EditText) solo.getView(R.id.uiBusStopIDInput);
        solo.clearEditText(busStopIdInput);
        solo.enterText(busStopIdInput, BUS_STOP_ID);

        Button retrievePredictionsButton = (Button) solo.getView(R.id.uiRetrievePredictions);
        solo.clickOnView(retrievePredictionsButton);

        solo.waitForView(R.id.uiBusPredictionDisplay);

        solo.scrollListToTop(0);
        for (int i = 0; i < expected.length; i++)
        {
            ArrayList<TextView> views = solo.clickInList(i + 1, 0);
            for (int j = 0; j < expected[i].length; j++)
            {
                String expectedStr = expected[i][j];
                TextView textView = views.get(j);
                solo.waitForView(textView);
                assertEquals(expectedStr, textView.getText().toString());
            }
        }

    }

    public static Date genRefTime() throws ParseException
    {
        return TimeConverter.parse("20130818 17:23");
    }

}