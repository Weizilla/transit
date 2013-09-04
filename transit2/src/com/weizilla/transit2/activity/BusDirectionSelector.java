package com.weizilla.transit2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.weizilla.transit2.R;
import com.weizilla.transit2.TransitService;
import com.weizilla.transit2.data.Route;
import com.weizilla.transit2.dataproviders.CTADataProvider;
import com.weizilla.transit2.dataproviders.TransitDataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * selects a bus direction
 *
 * @author wei
 *         Date: 9/2/13
 *         Time: 5:46 PM
 */
public class BusDirectionSelector extends Activity implements AdapterView.OnItemClickListener
{
    public static final String RETURN_INTENT_KEY = BusDirectionSelector.class.getName() + ".intent.key";
    private static final String TAG = "BusDirectionSelector";
    private TransitService transitService;
    private List<String> directionsDisplay;
    private List<Direction> directions;
    private ArrayAdapter<String> directionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.bus_direction_select);

        directionsDisplay = new ArrayList<String>();
        directions = new ArrayList<Direction>();
        directionsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                directionsDisplay);
        ListView uiRoutesDisplay = (ListView) findViewById(R.id.uiBusRouteList);
        uiRoutesDisplay.setAdapter(directionsAdapter);
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
        retrieveDirections(route);
    }

    private void retrieveDirections(String route)
    {
        new LookupRoutesTask().execute(route);
    }

    private void updateUI(List<Direction> directions)
    {
        directionsDisplay.clear();
        directionsValues.clear();

        for (Direction direction : directions)
        {
            directionsDisplay.add(direction.toString());
            directionsValues.add(direction);
            Log.d(TAG, "Adding direction: " + direction.toString());
        }

        directionsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String directionValue = directionsValues.get(position);
        returnDirection(directionValue);
    }

    private void returnDirection(String direction)
    {
        Intent result = new Intent();
        result.putExtra(RETURN_INTENT_KEY, direction);
        setResult(RESULT_OK, result);
        finish();
    }

    private class LookupDirectionTask extends AsyncTask<String, Void, List<Route>>
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
