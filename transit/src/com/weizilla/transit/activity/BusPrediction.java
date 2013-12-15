package com.weizilla.transit.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
    private static final int BUS_PICKER_RESULT = 1;
    private TransitService transitService;
    private BusPredictionAdapter predictionAdapter;
    private EditText busStopIdInput;
    private TransitDataProvider dataProvider;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initGui();

        dataProvider = getDataProvider();
        transitService = new TransitService(dataProvider);

        Intent intent = getIntent();
        StopList stopList = intent.getParcelableExtra(StopList.INTENT_KEY);
        List<Integer> stopIds = parseStopIds(stopList);
        updateUiStopIds(stopIds);
        refreshPredictions(stopIds);
    }

    private void initGui()
    {
        setContentView(R.layout.bus_pred);
        busStopIdInput = (EditText) findViewById(R.id.uiBusPredStopIdInput);
        predictionAdapter = new BusPredictionAdapter(this);
        ListView uiPredictionsDisplay = (ListView) findViewById(R.id.uiBusPredDisplay);
        uiPredictionsDisplay.setAdapter(predictionAdapter);
    }

    private TransitDataProvider getDataProvider()
    {
        String ctaApiKey = getString(R.string.ctaApiKey);
        Intent intent = getIntent();
        TransitDataProvider dataProvider =
                (TransitDataProvider) intent.getSerializableExtra(TransitDataProvider.KEY);
        return dataProvider != null ? dataProvider : new CTADataProvider(ctaApiKey);
    }

    private void updateUiStopIds(List<Integer> stopIds)
    {
        if (stopIds == null || stopIds.isEmpty())
        {
            return;
        }

        String stops = Joiner.on(" ").join(stopIds);
        busStopIdInput.setText(stops);
    }

    public void retrievePredictions(View view)
    {
        String stopIdStr = busStopIdInput.getText().toString();
        List<Integer> stopIds = parseStopIds(stopIdStr);
        if (! stopIds.isEmpty())
        {
            refreshPredictions(stopIds);
        }
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

    public void lookUpStop(View view)
    {
        Intent intent = new Intent();
        intent.setClass(this, BusStopPicker.class);
        intent.putExtra(TransitDataProvider.KEY, dataProvider);
        startActivityForResult(intent, BUS_PICKER_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == BUS_PICKER_RESULT && resultCode == Activity.RESULT_OK)
        {
            StopList stopList = data.getParcelableExtra(StopList.INTENT_KEY);
            List<Integer> stopIds = parseStopIds(stopList);
            updateUiStopIds(stopIds);
            refreshPredictions(stopIds);
        }
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

    private static List<Integer> parseStopIds(StopList stopList)
    {
        if (stopList == null || stopList.getStops().isEmpty())
        {
            return Collections.emptyList();
        }

        List<Integer> stopIds = new ArrayList<>();
        for (Stop stop : stopList.getStops())
        {
            stopIds.add(stop.getId());
        }
        return stopIds;
    }

    private static List<Integer> parseStopIds(String stopIds)
    {
        if (Strings.isNullOrEmpty(stopIds))
        {
            return Collections.emptyList();
        }

        List<Integer> stopIdList = new ArrayList<>();
        String[] stopsStr = stopIds.split(" ");
        for (String stopStr : stopsStr)
        {
            stopIdList.add(Integer.valueOf(stopStr));
        }
        return stopIdList;
    }

    private class LookupPredictionsTask extends AsyncTask<List<Integer>, Void, List<Prediction>>
    {
        private final ProgressDialog progressDialog;

        protected LookupPredictionsTask()
        {
            progressDialog = new ProgressDialog(BusPrediction.this);
            progressDialog.setMessage("Retrieving predictions...");
            progressDialog.setIndeterminate(true);
        }

        @Override
        protected void onPreExecute()
        {
            Log.d(TAG, "Retrieving predictions...");
            progressDialog.show();
        }

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
            progressDialog.dismiss();
        }
    }
}

