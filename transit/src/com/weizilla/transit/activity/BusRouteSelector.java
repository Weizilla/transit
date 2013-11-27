package com.weizilla.transit.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.weizilla.transit.ui.FilterTextWatcher;

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
    private static final String TAG = "transit.BusRouteSelector";
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

        favoriteRoutes.clear();
        retrievedRoutes.clear();

        TransitDataProvider transitDataProvider = getDataProvider();
        transitService = new TransitService(transitDataProvider);

        favRouteStore = new FavRouteStore(this);

        initGui();
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
        routesAdapter = new BusRouteAdapter(this);
        ListView uiRoutesDisplay = (ListView) findViewById(R.id.uiBusRouteList);
        uiRoutesDisplay.setAdapter(routesAdapter);
        uiRoutesDisplay.setOnItemClickListener(this);
        uiRoutesDisplay.setLongClickable(true);
        uiRoutesDisplay.setOnItemLongClickListener(this);
        busRouteInput = (EditText) findViewById(R.id.uiBusRouteInput);
        busRouteInput.addTextChangedListener(new FilterTextWatcher(routesAdapter));
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
        routesAdapter.clear();
        routesAdapter.addAll(favoriteRoutes);
        routesAdapter.addAll(retrievedRoutes);
        routesAdapter.notifyDataSetChanged();
        routesAdapter.getFilter().filter(busRouteInput.getText().toString());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Route route = routesAdapter.getItem(position);
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
        Route route = routesAdapter.getItem(position);
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

        builder.setItems(new String[]{menuString}, new DialogClickListener(route));
        builder.create().show();
    }

    private class DialogClickListener implements DialogInterface.OnClickListener
    {
        private Route route;

        private DialogClickListener(Route route)
        {
            this.route = route;
        }

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
    }

    private class RetrieveRoutesTask extends AsyncTask<Void, Void, List<Route>>
    {
        private final List<Route> routes;
        private final BusRoutesProvider provider;
        private final String providerName;

        private RetrieveRoutesTask(BusRoutesProvider provider, List<Route> routes)
        {
            this.provider = provider;
            this.routes = routes;
            providerName = provider.getClass().getName();
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
            Log.d(TAG, "Retrieved " + routes.size() + " routes from " + providerName);
            synchronized (this.routes)
            {
                this.routes.clear();
                this.routes.addAll(routes);
            }
            updateAllRoutes();
        }
    }
}
