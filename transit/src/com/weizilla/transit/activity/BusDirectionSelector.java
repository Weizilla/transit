package com.weizilla.transit.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.weizilla.transit.R;
import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Route;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * selects a bus direction
 *
 * @author wei
 *         Date: 9/2/13
 *         Time: 5:46 PM
 */
public class BusDirectionSelector extends TransitActivity
{
    private static final String TAG = "BusDirectionSelector";
    private Map<Direction, Button> directionButtons;
    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bus_direction_select);
        initGui();

        route = getIntent().getParcelableExtra(Route.KEY);
        disableAllDirection();
        retrieveDirections();
    }

    private void initGui()
    {
        directionButtons = new HashMap<>();
        initButton(Direction.Northbound, R.id.uiBusDirNorth);
        initButton(Direction.Southbound, R.id.uiBusDirSouth);
        initButton(Direction.Eastbound, R.id.uiBusDirEast);
        initButton(Direction.Westbound, R.id.uiBusDirWest);

        setProgressBar(R.id.uiBusDirProgress);
    }

    private void initButton(final Direction direction, int resId)
    {
        Button dirButton = (Button) findViewById(resId);
        directionButtons.put(direction, dirButton);
        dirButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                returnDirection(direction);
            }
        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        dismissProgress();
    }

    private void retrieveDirections()
    {
        if (route != null)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    new LookupDirectionTask().execute(route);
                }
            });
        }
    }

    private void updateUI(List<Direction> retrievedDirections)
    {
        disableAllDirection();
        for (Direction direction : retrievedDirections)
        {
            Button directionButton = directionButtons.get(direction);
            directionButton.setEnabled(true);
        }
    }

    private void disableAllDirection()
    {
        for (Button button : directionButtons.values())
        {
            button.setEnabled(false);
        }
    }

    private void returnDirection(Direction direction)
    {
        Intent result = new Intent();
        result.putExtra(Direction.KEY, (Parcelable) direction);
        setResult(RESULT_OK, result);
        finish();
    }

    private class LookupDirectionTask extends AsyncTask<Route, Void, List<Direction>>
    {
        @Override
        protected void onPreExecute()
        {
            Log.d(TAG, "Retrieving directions...");
            showProgress();
        }

        @Override
        protected List<Direction> doInBackground(Route... params)
        {
            Route route = params[0];
            return transitService.lookupDirections(route);
        }

        @Override
        protected void onPostExecute(List<Direction> directions)
        {
            super.onPostExecute(directions);
            updateUI(directions);
            dismissProgress();
        }
    }
}
