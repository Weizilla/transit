package com.weizilla.transit.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import com.weizilla.transit.R;
import com.weizilla.transit.TransitService;
import com.weizilla.transit.ui.BusRouteAdapter;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.dataproviders.CTADataProvider;
import com.weizilla.transit.dataproviders.TransitDataProvider;

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
    private List<Route> routes;
    private BusRouteAdapter routesAdapter;
    private EditText busRouteInput;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.bus_route_select);

        busRouteInput = (EditText) findViewById(R.id.uiBusRouteInput);

        routes = new ArrayList<Route>();
        routesAdapter = new BusRouteAdapter(this, routes);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Route route = routes.get(position);
        returnRoute(route);
    }

    private void updateUI(List<Route> routes)
    {
        //TODO is this necessary?
        this.routes.clear();
        this.routes.addAll(routes);

        if (Log.isLoggable(TAG, Log.DEBUG))
        {
            Log.d(TAG, "Added " + routes.size() + " routes");
        }

        routesAdapter.notifyDataSetChanged();

        hideKeyboard();
    }

    private void returnRoute(Route route)
    {
        Intent result = new Intent();
        result.putExtra(RETURN_INTENT_KEY, route.getId());
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
