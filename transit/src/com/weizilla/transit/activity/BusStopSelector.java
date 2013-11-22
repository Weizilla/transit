package com.weizilla.transit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.weizilla.transit.R;
import com.weizilla.transit.TransitService;
import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.dataproviders.CTADataProvider;
import com.weizilla.transit.dataproviders.TransitDataProvider;
import com.weizilla.transit.ui.BusStopAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * selects a bus stop
 *
 * @author wei
 *         Date: 9/2/13
 *         Time: 7:07 PM
 */
public class BusStopSelector extends Activity implements AdapterView.OnItemClickListener
{
    public static final String RETURN_INTENT_KEY = BusStopSelector.class.getName() + ".intent.key";
    private static final String TAG = "BusStopSelector";
    private TransitService transitService;
    private List<Stop> stops;
    private BusStopAdapter stopsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.bus_stop_select);

        stops = new ArrayList<>();
        stopsAdapter = new BusStopAdapter(this, stops);
        ListView uiStopsDisplay = (ListView) findViewById(R.id.uiBusStopList);
        uiStopsDisplay.setAdapter(stopsAdapter);
        uiStopsDisplay.setOnItemClickListener(this);

        transitService = new TransitService();

        String ctaApiKey = getString(R.string.ctaApiKey);

        Intent intent = getIntent();
        TransitDataProvider dataProvider =
                (TransitDataProvider) intent.getSerializableExtra(TransitDataProvider.KEY);
        if (dataProvider != null)
        {
            transitService.setDataProvider(dataProvider);
        }
        else
        {
            transitService.setDataProvider(new CTADataProvider(ctaApiKey));
        }

        String route = intent.getStringExtra(Route.KEY);
        Direction direction = intent.getParcelableExtra(Direction.KEY);
        retrieveStops(route, direction);
    }

    private void retrieveStops(String route, Direction direction)
    {
        new LookupStopTask().execute(route, direction);
    }

    private void updateUI(List<Stop> retrievedStops)
    {
        stops.clear();
        stops.addAll(retrievedStops);

        if (Log.isLoggable(TAG, Log.DEBUG))
        {
            Log.d(TAG, "Adding " + retrievedStops.size() + " stops");
        }

        stopsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Stop stop = stops.get(position);
        returnStop(stop);
    }

    private void returnStop(Stop stop)
    {
        Intent result = new Intent();
        result.putExtra(RETURN_INTENT_KEY, stop);
        setResult(RESULT_OK, result);
        finish();
    }

    private class LookupStopTask extends AsyncTask<Object, Void, List<Stop>>
    {
        @Override
        protected List<Stop> doInBackground(Object... params)
        {
            String route = (String) params[0];
            Direction direction = (Direction) params[1];
            return transitService.lookupStops(route, direction);
        }

        @Override
        protected void onPostExecute(List<Stop> stops)
        {
            super.onPostExecute(stops);
            updateUI(stops);
        }
    }
}
