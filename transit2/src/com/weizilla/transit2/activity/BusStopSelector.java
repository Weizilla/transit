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
        ListView uiRoutesDisplay = (ListView) findViewById(R.id.uiBusDirectionList);
        uiRoutesDisplay.setAdapter(stopsAdapter);
        uiRoutesDisplay.setOnItemClickListener(this);

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
        String direction = intent.getStringExtra(Direction.KEY);
        retrieveStops(route, direction);
    }

    private void retrieveStops(String route, String direction)
    {
        new LookupStopTask().execute(route, direction);
    }

    private void updateUI(List<Direction> retrievedDirections)
    {
        directionsDisplay.clear();
        directions.clear();

        for (Direction direction : retrievedDirections)
        {
            directionsDisplay.add(direction.toString());
            directions.add(direction);
            Log.d(TAG, "Adding direction: " + direction.toString());
        }

        directionsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Direction direction = directions.get(position);
        returnDirection(direction);
    }

    private void returnDirection(Direction direction)
    {
        Intent result = new Intent();
        result.putExtra(RETURN_INTENT_KEY, direction.getName());
        setResult(RESULT_OK, result);
        finish();
    }

    private class LookupDirectionTask extends AsyncTask<String, Void, List<Direction>>
    {

        @Override
        protected List<Direction> doInBackground(String... params) {
            String route = params[0];
            return transitService.lookupDirections(route);
        }

        @Override
        protected void onPostExecute(List<Direction> directions)
        {
            super.onPostExecute(directions);
            updateUI(directions);
        }
    }
}
