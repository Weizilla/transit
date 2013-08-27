package com.weizilla.transit2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;

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
        EditText busStopIdInput = (EditText) findViewById(R.id.uiBusStopIDInput);
        String busStopId = busStopIdInput.getText().toString();

        List<String> predictions = retrievePredictions(busStopId);
        ArrayAdapter<String> predictionsAdapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, predictions);

        ListView uiPredictionsDisplay = (ListView) findViewById(R.id.uiPredictionList);
        uiPredictionsDisplay.setAdapter(predictionsAdapter);
    }

    private List<String> retrievePredictions(String busStopId)
    {
        return Collections.singletonList(busStopId);
    }
}