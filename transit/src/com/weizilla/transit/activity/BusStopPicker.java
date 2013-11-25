package com.weizilla.transit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;
import com.weizilla.transit.R;
import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.dataproviders.TransitDataProvider;

/**
 * launches child activites for selecting route attributes and displays the currently selected ones
 *
 * @author wei
 *         Date: 9/2/13
 *         Time: 7:07 PM
 */
public class BusStopPicker extends Activity
{
    private static final int ROUTE_REQUEST = 1;
    private static final int DIRECTION_REQUEST = 2;
    private static final int STOP_REQUEST = 3;

    private TextView uiSelectedRoute;
    private TextView uiSelectedDirection;
    private TextView uiSelectedStop;
    private Route selectedRoute;
    private Direction selectedDirection;
    private Stop selectedStop;
    private TransitDataProvider dataProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        dataProvider = (TransitDataProvider) getIntent().getSerializableExtra(TransitDataProvider.KEY);

        this.setContentView(R.layout.bus_stop_picker);
        this.uiSelectedRoute = (TextView) findViewById(R.id.uiSelectedRoute);
        this.uiSelectedDirection = (TextView) findViewById(R.id.uiSelectedDirection);
        this.uiSelectedStop = (TextView) findViewById(R.id.uiSelectedStop);

        startNextStep(null);
    }

    public void startNextStep(View view)
    {
        if (selectedRoute == null)
        {
            startSelectRouteActivity();
        }
        else if (selectedDirection == null)
        {
            startSelectDirectionActivity();
        }
        else if (selectedStop == null)
        {
            startSelectStopActivity();
        }
        else
        {
            startBusPredictionActivity();
        }
    }

    private void startSelectRouteActivity()
    {
        Intent intent = new Intent();
        intent.putExtra(TransitDataProvider.KEY, dataProvider);
        intent.setClass(this, BusRouteSelector.class);
        startActivityForResult(intent, ROUTE_REQUEST);
    }

    private void startSelectDirectionActivity()
    {
        Intent intent = new Intent();
        intent.putExtra(TransitDataProvider.KEY, dataProvider);
        intent.setClass(this, BusDirectionSelector.class);
        intent.putExtra(Route.KEY, selectedRoute);
        startActivityForResult(intent, DIRECTION_REQUEST);
    }

    private void startSelectStopActivity()
    {
        Intent intent = new Intent();
        intent.putExtra(TransitDataProvider.KEY, dataProvider);
        intent.setClass(this, BusStopSelector.class);
        intent.putExtra(Route.KEY, selectedRoute);
        intent.putExtra(Direction.KEY, (Parcelable) selectedDirection);
        startActivityForResult(intent, STOP_REQUEST);
    }

    private void startBusPredictionActivity()
    {
        Intent intent = new Intent();
        intent.putExtra(TransitDataProvider.KEY, dataProvider);
        intent.setClass(this, BusPrediction.class);
        intent.putExtra(Stop.KEY, selectedStop);
        startActivity(intent);
    }

    public void startOver(View view)
    {
        this.selectedRoute = null;
        this.uiSelectedRoute.setText("Route:");
        this.selectedDirection = null;
        this.uiSelectedDirection.setText("Direction:");
        this.selectedStop = null;
        this.uiSelectedStop.setText("Stop:");
    }

    private void setSelectedRoute(Route route)
    {
        this.selectedRoute = route;
        this.uiSelectedRoute.setText("Route: " + this.selectedRoute);
    }

    private void setSelectedDirection(Direction direction)
    {
        this.selectedDirection = direction;
        this.uiSelectedDirection.setText("Direction: " + this.selectedDirection);
    }

    private void setSelectedStop(Stop stop)
    {
        this.selectedStop = stop;
        this.uiSelectedStop.setText("Stop: " + this.selectedStop.getName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == ROUTE_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                Route route = data.getParcelableExtra(BusRouteSelector.RETURN_INTENT_KEY);
                setSelectedRoute(route);
                startNextStep(null);
            }
        }
        else if (requestCode == DIRECTION_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                Direction direction = data.getParcelableExtra(BusDirectionSelector.RETURN_INTENT_KEY);
                setSelectedDirection(direction);
                startNextStep(null);
            }
        }
        else if (requestCode == STOP_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                Stop stop = data.getParcelableExtra(BusStopSelector.RETURN_INTENT_KEY);
                setSelectedStop(stop);
                startNextStep(null);
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
