package com.weizilla.transit.activity;

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
import com.weizilla.transit.BusStopsProvider;
import com.weizilla.transit.R;
import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.db.FavStopStore;
import com.weizilla.transit.ui.BusStopAdapter;
import com.weizilla.transit.ui.FilterTextWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * selects a bus stop
 *
 * @author wei
 *         Date: 9/2/13
 *         Time: 7:07 PM
 */
public class BusStopSelector extends TransitActivity
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener
{
    public static final int FAV_BACKGROUND_COLOR = Color.GREEN;
    private static final String TAG = "transit.BusStopSelector";
    private final List<Stop> retrievedStops = new ArrayList<>();
    private final List<Stop> favoriteStops = new ArrayList<>();
    private FavStopStore favStopStore;
    private BusStopAdapter stopsAdapter;
    private Route route;
    private Direction direction;
    private EditText busStopInput;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        favStopStore = new FavStopStore(this);

        Intent intent = getIntent();
        route = intent.getParcelableExtra(Route.KEY);
        direction = intent.getParcelableExtra(Direction.KEY);

        initGui();
    }

    private void initGui()
    {
        this.setContentView(R.layout.bus_stop_select);
        stopsAdapter = new BusStopAdapter(this);
        busStopInput = (EditText) findViewById(R.id.uiBusStopInput);
        busStopInput.addTextChangedListener(new FilterTextWatcher(stopsAdapter));
        ListView uiStopsDisplay = (ListView) findViewById(R.id.uiBusStopList);
        uiStopsDisplay.setAdapter(stopsAdapter);
        uiStopsDisplay.setOnItemClickListener(this);
        uiStopsDisplay.setLongClickable(true);
        uiStopsDisplay.setOnItemLongClickListener(this);

        setProgressBar(R.id.uiBusStopProgress);
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
        dismissProgress();
    }

    private void retrieveStops()
    {
        if (route != null || direction != null)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    new RetrieveStopsTask(retrievedStops).execute(transitService, route, direction);
                }
            });
        }
    }

    public void refreshFavorites()
    {
        if (route != null || direction != null)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    new RetrieveStopsTask(favoriteStops).execute(favStopStore, route, direction);
                }
            });
        }
    }

    private void updateAllStops()
    {
        stopsAdapter.clear();
        stopsAdapter.addAll(favoriteStops);
        stopsAdapter.addAll(retrievedStops);
        stopsAdapter.getFilter().filter(busStopInput.getText().toString());
        stopsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Stop stop = stopsAdapter.getItem(position);
        finishWithStop(stop);
    }

    private void finishWithStop(Stop stop)
    {
        Intent result = new Intent();
        result.putExtra(Stop.KEY, stop);
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        Stop stop = stopsAdapter.getItem(position);
        showContextMenu(stop);
        return true;
    }

    private void showContextMenu(Stop stop)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(stop.getName());

        String menuString = stop.isFavorite()
            ? "Remove from Favorites"
            : "Add to Favorites";

        builder.setItems(new String[]{menuString}, buildOnClickListener(stop));
        builder.create().show();
    }

    private DialogInterface.OnClickListener buildOnClickListener(final Stop stop)
    {
        return new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (stop.isFavorite())
                {
                    favStopStore.removeStop(stop);
                }
                else
                {
                    favStopStore.addStop(route, direction, stop);
                }
                refreshFavorites();
            }
        };
    }

    private class RetrieveStopsTask extends AsyncTask<Object, Void, List<Stop>>
    {
        private final List<Stop> stops;

        private RetrieveStopsTask(List <Stop> stops)
        {
            this.stops = stops;
        }

        @Override
        protected void onPreExecute()
        {
            Log.d(TAG, "Retrieving stops...");
            showProgress();
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
        protected void onPostExecute(List<Stop> stops)
        {
            super.onPostExecute(stops);
            synchronized (this.stops)
            {
                this.stops.clear();
                this.stops.addAll(stops);
            }
            updateAllStops();
            dismissProgress();
            Log.d(TAG, "Updating with " + stops.size() + " stops from provider");
        }
    }
}
