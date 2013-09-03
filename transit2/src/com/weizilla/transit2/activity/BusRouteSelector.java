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
 * selects a bus route
 *
 * @author wei
 *         Date: 9/2/13
 *         Time: 5:46 PM
 */
public class BusRouteSelector extends Activity implements AdapterView.OnItemClickListener
{
    public static final String RETURN_INTENT_KEY = BusRouteSelector.class.getName() + ".intent.key";
    private static final String TAG = "BusRouteSelector";
    private TransitService transitService;
    private List<String> routesDisplay;
    private ArrayAdapter<String> routesAdapter;
    private EditText busRouteInput;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.bus_route_select);

        busRouteInput = (EditText) findViewById(R.id.uiBusRouteInput);

        routesDisplay = new ArrayList<String>();
        routesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, routesDisplay);
        ListView uiRoutesDisplay = (ListView) findViewById(R.id.uiBusRouteList);
        uiRoutesDisplay.setAdapter(routesAdapter);
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

        retrieveRoutes();
    }

    private void retrieveRoutes()
    {
        new LookupRoutesTask().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String route = routesAdapter.getItem(position);
        returnRoute(route);
    }

    private void updateUI(List<Route> routes)
    {
        routesDisplay.clear();

        for (Route route : routes)
        {
            routesDisplay.add(route.toString());
            Log.d(TAG, "Adding route: " + route.toString());
        }

        routesAdapter.notifyDataSetChanged();

        hideKeyboard();
    }

    private void returnRoute(String route)
    {
        Intent result = new Intent();
        result.putExtra(RETURN_INTENT_KEY, route);
        setResult(RESULT_OK, result);
        finish();
    }

    private void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(busRouteInput.getWindowToken(), 0);
    }

    private class LookupRoutesTask extends AsyncTask<Void, Void, List<Route>>
    {

        @Override
        protected List<Route> doInBackground(Void... params) {
            return transitService.lookupRoutes();
        }

        @Override
        protected void onPostExecute(List<Route> routes)
        {
            super.onPostExecute(routes);
            updateUI(routes);
        }
    }
}
