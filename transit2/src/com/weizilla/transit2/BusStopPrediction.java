package com.weizilla.transit2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Activity for finding the predictions for a single bus stop
 *
 * @author Wei Yang
 * Date: 8/26/13
 * Time: 7:26 PM
 */
public class BusStopPrediction extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.bus_prediction);
    }

    public void lookupPredictions(View view)
    {
        // do prediction look up here
    }
}