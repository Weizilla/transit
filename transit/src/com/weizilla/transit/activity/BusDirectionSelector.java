package com.weizilla.transit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import com.weizilla.transit.R;
import com.weizilla.transit.TransitService;
import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.dataproviders.CTADataProvider;
import com.weizilla.transit.dataproviders.TransitDataProvider;

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
public class BusDirectionSelector extends Activity
{
    public static final String RETURN_INTENT_KEY = BusDirectionSelector.class.getName() + ".intent.key";
    private static final String TAG = "BusDirectionSelector";
    private TransitService transitService;
    private Map<Direction, Button> directionButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.bus_direction_select);
        initButtons();

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
        disableAllDirection();
        retrieveDirections(route);
    }

    private void initButtons()
    {
        directionButtons = new HashMap<>();
        initButton(Direction.Northbound, R.id.uiBusDirNorth);
        initButton(Direction.Southbound, R.id.uiBusDirSouth);
        initButton(Direction.Eastbound, R.id.uiBusDirEast);
        initButton(Direction.Westbound, R.id.uiBusDirWest);
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

    private void retrieveDirections(String route)
    {
        new LookupDirectionTask().execute(route);
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
        result.putExtra(RETURN_INTENT_KEY, (Parcelable) direction);
        setResult(RESULT_OK, result);
        finish();
    }

    private class LookupDirectionTask extends AsyncTask<String, Void, List<Direction>>
    {

        @Override
        protected List<Direction> doInBackground(String... params)
        {
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
