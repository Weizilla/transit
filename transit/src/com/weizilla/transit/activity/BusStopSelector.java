package com.weizilla.transit.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.weizilla.transit.BusStopsProvider;
import com.weizilla.transit.R;
import com.weizilla.transit.TransitService;
import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.dataproviders.CTADataProvider;
import com.weizilla.transit.dataproviders.TransitDataProvider;
import com.weizilla.transit.db.FavStopStore;
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
    public static final int FAV_BACKGROUND_COLOR = Color.GREEN;
    private static final String TAG = "BusStopSelector";
    private final List<Stop> allStops = new ArrayList<>();
    private final List<Stop> retrievedStops = new ArrayList<>();
    private final List<Stop> favoriteStops = new ArrayList<>();
    private TransitService transitService;
    private FavStopStore favStopStore;
    private BusStopAdapter stopsAdapter;
    private Route route;
    private Direction direction;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        TransitDataProvider transitDataProvider = getDataProvider();
        transitService = new TransitService(transitDataProvider);

        favStopStore = new FavStopStore(this);

        Intent intent = getIntent();
        route = intent.getParcelableExtra(Route.KEY);
        direction = intent.getParcelableExtra(Direction.KEY);

        initGui();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        favStopStore.open();

        //TODO must occur after. do we really want to refresh automatically
        // when screne is activated?
        refreshFavorites();
        retrieveStops();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        favStopStore.close();
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
        this.setContentView(R.layout.bus_stop_select);
        stopsAdapter = new BusStopAdapter(this, allStops);
        ListView uiStopsDisplay = (ListView) findViewById(R.id.uiBusStopList);
        uiStopsDisplay.setAdapter(stopsAdapter);
        uiStopsDisplay.setOnItemClickListener(this);
    }

    private void retrieveStops()
    {
        if (route != null || direction != null)
        {
            new RetrieveStopsTask(retrievedStops).execute(transitService, route, direction);
        }
    }

    public void refreshFavorites()
    {
        if (route != null || direction != null)
        {
            new RetrieveStopsTask(favoriteStops).execute(favStopStore, route, direction);
        }
    }

    private void updateAllStops()
    {
        synchronized (allStops)
        {
            allStops.clear();
            allStops.addAll(favoriteStops);
            allStops.addAll(retrievedStops);
        }

        stopsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Stop stop = allStops.get(position);
        finishWithStop(stop);
    }

    private void finishWithStop(Stop stop)
    {
        Intent result = new Intent();
        result.putExtra(RETURN_INTENT_KEY, stop);
        setResult(RESULT_OK, result);
        finish();
    }

    private class RetrieveStopsTask extends AsyncTask<Object, Void, List<Stop>>
    {
        private final List<Stop> stops;

        private RetrieveStopsTask(List <Stop> stops)
        {
            this.stops = stops;
        }

        @Override
        protected List<Stop> doInBackground(Object... params)
        {
            BusStopsProvider provider = (BusStopsProvider) params[0];
            Route route = (Route) params[1];
            Direction direction = (Direction) params[2];

            List<Stop> stops = provider.getStops(route, direction);

            String name = provider.getClass().getName();
            Log.d(TAG, "Got " + stops.size() + " stops  from provider " + name);

            return stops;
        }

        @Override
        protected void onPostExecute(List <Stop> stops)
        {
            super.onPostExecute(stops);
            synchronized (this.stops)
            {
                this.stops.clear();
                this.stops.addAll(stops);
            }
            updateAllStops();
        }
    }
}
