package com.weizilla.transit.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import com.weizilla.transit.R;
import com.weizilla.transit.TransitService;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.dataproviders.CTADataProvider;
import com.weizilla.transit.dataproviders.TransitDataProvider;
import com.weizilla.transit.db.FavRouteStore;
import com.weizilla.transit.ui.BusRouteAdapter;

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
    private FavRouteStore favRouteStore;
    private List<Route> allRoutes;
    private List<Route> favoriteRoutes;
    private List<Route> retrievedRoutes;
    private BusRouteAdapter routesAdapter;
    private EditText busRouteInput;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        allRoutes = new ArrayList<>();
        favoriteRoutes = new ArrayList<>();
        retrievedRoutes = new ArrayList<>();

        TransitDataProvider transitDataProvider = getDataProvider();
        transitService = new TransitService(transitDataProvider);

        favRouteStore = new FavRouteStore(this);

        initGui();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        favRouteStore.open();

        //TODO must occur after
        refreshFavorites();
        retrieveRoutes();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        favRouteStore.close();
    }

    private TransitDataProvider getDataProvider()
    {
        String ctaApiKey = getString(R.string.ctaApiKey);
        Intent intent = getIntent();
        TransitDataProvider dataProvider =
                (TransitDataProvider) intent.getSerializableExtra(TransitDataProvider.KEY);
        return dataProvider != null ? dataProvider : new CTADataProvider(ctaApiKey);
    }

    private void initGui()
    {
        setContentView(R.layout.bus_route_select);
        busRouteInput = (EditText) findViewById(R.id.uiBusRouteInput);
        routesAdapter = new BusRouteAdapter(this, allRoutes);
        ListView uiRoutesDisplay = (ListView) findViewById(R.id.uiBusRouteList);
        uiRoutesDisplay.setAdapter(routesAdapter);
        uiRoutesDisplay.setOnItemClickListener(this);
    }

    private void retrieveRoutes()
    {
        new LookupRoutesTask().execute();
    }

    public void refreshFavorites()
    {
        new RefreshFavoritesTask().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Route route = allRoutes.get(position);
        returnRoute(route);
    }

    private void returnRoute(Route route)
    {
        Intent result = new Intent();
        result.putExtra(RETURN_INTENT_KEY, route.getId());
        setResult(RESULT_OK, result);
        finish();
    }

    private void updateAllRoutes()
    {
        synchronized (allRoutes)
        {
            allRoutes.clear();
            allRoutes.addAll(favoriteRoutes);
            allRoutes.addAll(retrievedRoutes);
        }

        routesAdapter.notifyDataSetChanged();
        hideKeyboard();
    }

    private void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(busRouteInput.getWindowToken(), 0);
    }

    private class LookupRoutesTask extends AsyncTask<Void, Void, List<Route>>
    {
        @Override
        protected List<Route> doInBackground(Void... params)
        {
            return transitService.lookupRoutes();
        }

        @Override
        protected void onPostExecute(List<Route> routes)
        {
            super.onPostExecute(routes);
            synchronized (retrievedRoutes)
            {
                retrievedRoutes.clear();
                retrievedRoutes.addAll(routes);
            }
            updateAllRoutes();
        }
    }

    private class RefreshFavoritesTask extends AsyncTask<Void, Void, List<Route>>
    {
        @Override
        protected  List<Route> doInBackground(Void... params)
        {
            return favRouteStore.getRoutes();
        }

        @Override
        protected void onPostExecute(List<Route> routes)
        {
            super.onPostExecute(routes);
            synchronized (favoriteRoutes)
            {
                favoriteRoutes.clear();
                favoriteRoutes.addAll(routes);
            }
            updateAllRoutes();
        }
    }
}
