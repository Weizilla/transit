package com.weizilla.transit.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import com.weizilla.transit.R;
import com.weizilla.transit.TransitService;
import com.weizilla.transit.data.Prediction;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.dataproviders.CTADataProvider;
import com.weizilla.transit.dataproviders.TransitDataProvider;
import com.weizilla.transit.ui.BusPredictionAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Activity for finding the predictions for a single bus stop
 *
 * @author Wei Yang
 *         Date: 8/26/13
 *         Time: 7:26 PM
 */
public class BusPrediction extends Activity
{
    private static final String TAG = "BusPrediction";
    private TransitService transitService;
    private List<Prediction> predictions;
    private BusPredictionAdapter predictionAdapter;
    private EditText busStopIdInput;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.bus_pred);

        busStopIdInput = (EditText) findViewById(R.id.uiBusStopIDInput);

        predictions = new ArrayList<>();
        predictionAdapter = new BusPredictionAdapter(this, predictions);
        ListView uiPredictionsDisplay = (ListView) findViewById(R.id.uiPredictionList);
        uiPredictionsDisplay.setAdapter(predictionAdapter);

        TransitDataProvider transitDataProvider = getDataProvider();
        transitService = new TransitService(transitDataProvider);

        Intent intent = getIntent();
        if (intent != null)
        {
            Stop stop = intent.getParcelableExtra(Stop.KEY);
            if (stop != null)
            {
                busStopIdInput.setText(String.valueOf(stop.getId()));
                retrievePredictions(null);
            }
        }
    }

    private TransitDataProvider getDataProvider()
    {
        String ctaApiKey = getString(R.string.ctaApiKey);
        Intent intent = getIntent();
        TransitDataProvider dataProvider =
                (TransitDataProvider) intent.getSerializableExtra(TransitDataProvider.KEY);
        return dataProvider != null ? dataProvider : new CTADataProvider(ctaApiKey);
    }

    public void retrievePredictions(View view)
    {
        int busStopId = Integer.parseInt(busStopIdInput.getText().toString());
        new LookupPredictionsTask().execute(busStopId);
    }

    private void updateUI(List<Prediction> retrievedPredictions)
    {
        predictions.clear();
        predictions.addAll(retrievedPredictions);

        if (Log.isLoggable(TAG, Log.DEBUG))
        {
            Log.d(TAG, "Added " + retrievedPredictions.size() + " predictions");
        }

        predictionAdapter.notifyDataSetChanged();

        hideKeyboard();
    }

    private void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(busStopIdInput.getWindowToken(), 0);
    }

    public void setRefTime(Date refTime)
    {
        predictionAdapter.setRefTime(refTime);
    }

    private class LookupPredictionsTask extends AsyncTask<Integer, Void, List<Prediction>>
    {
        @Override
        protected List<Prediction> doInBackground(Integer... params)
        {
            return transitService.lookupPredictions(params[0]);
        }

        @Override
        protected void onPostExecute(List<Prediction> predictions)
        {
            super.onPostExecute(predictions);

            updateUI(predictions);
        }
    }
}

