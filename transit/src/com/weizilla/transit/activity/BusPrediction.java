package com.weizilla.transit.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.weizilla.transit.R;
import com.weizilla.transit.TransitService;
import com.weizilla.transit.data.Prediction;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.data.StopList;
import com.weizilla.transit.dataproviders.CTADataProvider;
import com.weizilla.transit.dataproviders.TransitDataProvider;
import com.weizilla.transit.ui.BusPredictionAdapter;

import java.util.ArrayList;
import java.util.Collections;
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
    private static final String TAG = "transit.BusPrediction";
    private TransitService transitService;
    private BusPredictionAdapter predictionAdapter;
    private EditText busStopIdInput;
    private StopList stopList;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.bus_pred);

        busStopIdInput = (EditText) findViewById(R.id.uiBusStopIDInput);

        predictionAdapter = new BusPredictionAdapter(this);
        ListView uiPredictionsDisplay = (ListView) findViewById(R.id.uiBusPredictionDisplay);
        uiPredictionsDisplay.setAdapter(predictionAdapter);

        TransitDataProvider transitDataProvider = getDataProvider();
        transitService = new TransitService(transitDataProvider);

        Intent intent = getIntent();
        StopList stopList = intent.getParcelableExtra(StopList.INTENT_KEY);
        updateUiStopIds(stopList);
        retrievePredictions(null);
    }

    private TransitDataProvider getDataProvider()
    {
        String ctaApiKey = getString(R.string.ctaApiKey);
        Intent intent = getIntent();
        TransitDataProvider dataProvider =
                (TransitDataProvider) intent.getSerializableExtra(TransitDataProvider.KEY);
        return dataProvider != null ? dataProvider : new CTADataProvider(ctaApiKey);
    }

    private void updateUiStopIds(StopList stopList)
    {
        if (stopList == null || stopList.getStops().isEmpty())
        {
            return;
        }

        List<Integer> stopIds = new ArrayList<>();
        for (Stop stop : stopList.getStops())
        {
            stopIds.add(stop.getId());
        }
        String stops = Joiner.on(" ").join(stopIds);
        busStopIdInput.setText(stops);
    }

    public void retrievePredictions(View view)
    {
        List<Integer> stopIds = parseUiStopIds();
        if (! stopIds.isEmpty())
        {
            refreshPredictions(stopIds);
        }
    }

    private List<Integer> parseUiStopIds()
    {
        String stops = busStopIdInput.getText().toString();
        if (Strings.isNullOrEmpty(stops))
        {
            return Collections.emptyList();
        }

        List<Integer> stopIds = new ArrayList<>();
        String[] stopsStr = stops.split(" ");
        for (String stopStr : stopsStr)
        {
            stopIds.add(Integer.valueOf(stopStr));
        }
        return stopIds;
    }

    @SuppressWarnings({"unchecked", "varargs"})
    private void refreshPredictions(List<Integer> stopIds)
    {
        new LookupPredictionsTask().execute(stopIds);
    }

    private void updateUiPredictions(List<Prediction> retrievedPredictions)
    {
        predictionAdapter.clear();
        predictionAdapter.addAll(retrievedPredictions);

        // for future filtering based on bus route
        predictionAdapter.getFilter().filter(null);

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

    private class LookupPredictionsTask extends AsyncTask<List<Integer>, Void, List<Prediction>>
    {
        @Override
        @SuppressWarnings({"unchecked", "varargs"})
        protected List<Prediction> doInBackground(List<Integer> ... params)
        {
            List<Integer> stopIds = params[0];
            List<Integer> routeIds = Collections.emptyList();
            return transitService.lookupPredictions(stopIds, routeIds);
        }

        @Override
        protected void onPostExecute(List<Prediction> predictions)
        {
            super.onPostExecute(predictions);

            updateUiPredictions(predictions);
        }
    }
}

