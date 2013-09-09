package com.weizilla.transit2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.weizilla.transit2.R;
import com.weizilla.transit2.TransitService;
import com.weizilla.transit2.data.Direction;
import com.weizilla.transit2.data.Route;
import com.weizilla.transit2.data.Stop;
import com.weizilla.transit2.dataproviders.CTADataProvider;
import com.weizilla.transit2.dataproviders.TransitDataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * launches child activites for selecting route attributes and displays the currently selected ones
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
    private List<String> stopsDisplay;
    private List<Stop> stops;
    private ArrayAdapter<String> stopsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.bus_stop_select);

        stopsDisplay = new ArrayList<String>();
        stops = new ArrayList<Stop>();
        stopsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                stopsDisplay);
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
        //use better key
        Direction direction = intent.getParcelableExtra("DIRECTION");
        retrieveStops(route, direction);
    }

    private void retrieveStops(String route, Direction direction)
    {
        new LookupStopTask().execute(route, direction.name());
    }

    private void updateUI(List<Stop> retrievedStops)
    {
        stopsDisplay.clear();
        stops.clear();

        for (Stop stop : retrievedStops)
        {
            stopsDisplay.add(stop.toString());
            stops.add(stop);
            Log.d(TAG, "Adding stop: " + stop.toString());
        }

        stopsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Stop stop = stops.get(position);
        returnStop(stop);
    }

    private void returnStop(Stop stop)
    {
        Intent result = new Intent();
        result.putExtra(RETURN_INTENT_KEY, stop.getId());
        setResult(RESULT_OK, result);
        finish();
    }

    private class LookupStopTask extends AsyncTask<String, Void, List<Stop>>
    {

        @Override
        protected List<Stop> doInBackground(String... params) {
            String route = params[0];
            String direction = params[1];
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
