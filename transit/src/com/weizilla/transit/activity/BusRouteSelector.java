package com.weizilla.transit.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import com.weizilla.transit.BusRoutesProvider;
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
public class BusRouteSelector extends Activity
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener
{
    public static final String RETURN_INTENT_KEY = BusRouteSelector.class.getName() + ".intent.key";
    public static final int FAV_BACKGROUND_COLOR = Color.GREEN;
    private static final String TAG = "BusRouteSelector";
    private final List<Route> allRoutes = new ArrayList<>();
    private final List<Route> favoriteRoutes = new ArrayList<>();
    private final List<Route> retrievedRoutes = new ArrayList<>();
    private TransitService transitService;
    private FavRouteStore favRouteStore;
    private BusRouteAdapter routesAdapter;
    private EditText busRouteInput;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        allRoutes.clear();
        favoriteRoutes.clear();
        retrievedRoutes.clear();

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

        //TODO must occur after. do we really want to refresh automatically
        // when screne is activated?
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
        uiRoutesDisplay.setLongClickable(true);
        uiRoutesDisplay.setOnItemLongClickListener(this);
    }

    private void retrieveRoutes()
    {
        new RetrieveRoutesTask(transitService, retrievedRoutes).execute();
    }

    public void refreshFavorites()
    {
        new RetrieveRoutesTask(favRouteStore, favoriteRoutes).execute();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Route route = allRoutes.get(position);
        finishWithRoute(route);
    }

    private void finishWithRoute(Route route)
    {
        Intent result = new Intent();
        result.putExtra(RETURN_INTENT_KEY, route);
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        Route route = allRoutes.get(position);
        showContextMenu(route);
        return true;
    }

    private void showContextMenu(Route route)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(route.getId() + " - " + route.getName());

        String menuString = route.isFavorite()
            ? "Remove from Favorites"
            : "Add to Favorites";

        builder.setItems(new String[]{menuString}, buildOnClickListener(route));
        builder.create().show();
    }

    private DialogInterface.OnClickListener buildOnClickListener(final Route route)
    {
        return new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (route.isFavorite())
                {
                    favRouteStore.removeRoute(route);
                }
                else
                {
                    favRouteStore.addRoute(route);
                }
                refreshFavorites();
            }

        };
    }

    private void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(busRouteInput.getWindowToken(), 0);
    }

    private class RetrieveRoutesTask extends AsyncTask<Void, Void, List<Route>>
    {
        private final List<Route> routes;
        private BusRoutesProvider provider;

        private RetrieveRoutesTask(BusRoutesProvider provider, List<Route> routes)
        {
            this.provider = provider;
            this.routes = routes;
        }

        @Override
        protected List<Route> doInBackground(Void... params)
        {
            return provider.getRoutes();
        }

        @Override
        protected void onPostExecute(List<Route> routes)
        {
            super.onPostExecute(routes);
            synchronized (this.routes)
            {
                this.routes.clear();
                this.routes.addAll(routes);
            }
            updateAllRoutes();
        }
    }
}
