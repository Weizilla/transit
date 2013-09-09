package com.weizilla.transit2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.weizilla.transit2.R;
import com.weizilla.transit2.TransitService;
import com.weizilla.transit2.data.Prediction;
import com.weizilla.transit2.data.Stop;
import com.weizilla.transit2.dataproviders.CTADataProvider;
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
    private ArrayAdapter<String> predictionsAdapter;
    private EditText busStopIdInput;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.bus_prediction);

        busStopIdInput = (EditText) findViewById(R.id.uiBusStopIDInput);

        predictionsDisplay = new ArrayList<String>();
        predictionsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, predictionsDisplay);
        ListView uiPredictionsDisplay = (ListView) findViewById(R.id.uiPredictionList);
        uiPredictionsDisplay.setAdapter(predictionsAdapter);

        transitService = new TransitService();

        String ctaApiKey = getString(R.string.ctaApiKey);
        transitService.setDataProvider(new CTADataProvider(ctaApiKey));

        Intent intent = getIntent();
        if (intent != null)
        {
            Stop stop = intent.getParcelableExtra(Stop.KEY);
            if (stop != null)
            {
                busStopIdInput.setText(String.valueOf(stop.getId()));
            }
        }
    }

    public void retrievePredictions(View view)
    {
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

        hideKeyboard();
    }

    private void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(busStopIdInput.getWindowToken(), 0);
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

