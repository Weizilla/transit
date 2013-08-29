package com.weizilla.transit2.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.weizilla.transit2.R;
import com.weizilla.transit2.TransitService;
import com.weizilla.transit2.data.Prediction;
import com.weizilla.transit2.dataproviders.TransitDataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for finding the predictions for a single bus stop
 *
 * @author Wei Yang
 * Date: 8/26/13
 * Time: 7:26 PM
 */
public class BusStopPrediction extends Activity {
    private static final String TAG = "BusStopPrediction";
    private TransitService transitService;
    private List<String> predictionsDisplay;
    private String ctaApiKey;
    private ArrayAdapter<String> predictionsAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.bus_prediction);

        this.ctaApiKey = getString(R.string.ctaApiKey);

        transitService = new TransitService();

        predictionsDisplay = new ArrayList<String>();
        predictionsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, predictionsDisplay);
        ListView uiPredictionsDisplay = (ListView) findViewById(R.id.uiPredictionList);
        uiPredictionsDisplay.setAdapter(predictionsAdapter);
    }

    public void retrievePredictions(View view)
    {
        EditText busStopIdInput = (EditText) findViewById(R.id.uiBusStopIDInput);
        int busStopId = Integer.parseInt(busStopIdInput.getText().toString());

        new LookupPredictionsTask().execute(busStopId);
    }

    private void updateUI(List<Prediction> predictions)
    {
        predictionsDisplay.clear();

        for (Prediction prediction : predictions)
        {
            predictionsDisplay.add(prediction.toString());
            Log.d(TAG, "Adding Prediction: " + prediction.toString());
        }

        predictionsAdapter.notifyDataSetChanged();
    }

    public void setTransitDataProvider(TransitDataProvider transitDataProvider)
    {
        transitService.setDataProvider(transitDataProvider);
    }

    private class LookupPredictionsTask extends AsyncTask<Integer, Void, List<Prediction>>
    {
        @Override
        protected List<Prediction> doInBackground(Integer... params) {
            return transitService.lookupPredictions(params[0]);
        }

        @Override
        protected void onPostExecute(List<Prediction> predictions) {
            super.onPostExecute(predictions);

            updateUI(predictions);
        }
    }
}

