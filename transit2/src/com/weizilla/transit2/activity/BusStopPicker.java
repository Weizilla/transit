package com.weizilla.transit2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;
import com.weizilla.transit2.R;
import com.weizilla.transit2.data.Direction;
import com.weizilla.transit2.data.Route;
import com.weizilla.transit2.data.Stop;

/**
 * launches child activites for selecting route attributes and displays the currently selected ones
 *
 * @author wei
 *         Date: 9/2/13
 *         Time: 7:07 PM
 */
public class BusStopPicker extends Activity
{
    private TextView uiSelectedRoute;
    private TextView uiSelectedDirection;
    private TextView uiSelectedStop;
    private String selectedRoute;
    private Direction selectedDirection;
    private int selectedStop = -1;

    private static final int ROUTE_REQUEST = 1;
    private static final int DIRECTION_REQUEST = 2;
    private static final int STOP_REQUEST = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.bus_stop_picker);

        this.uiSelectedRoute = (TextView) findViewById(R.id.uiSelectedRoute);
        this.uiSelectedDirection = (TextView) findViewById(R.id.uiSelectedDirection);
        this.uiSelectedStop = (TextView) findViewById(R.id.uiSelectedStop);
    }

    public void selectStopStep(View view)
    {
        if (selectedRoute == null)
        {
            startSelectRouteActivity();
        }
        else if (selectedDirection == null)
        {
            startSelectDirectionActivity();
        }
        else if (selectedStop == -1)
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
        intent.setClass(this, BusRouteSelector.class);
        startActivityForResult(intent, ROUTE_REQUEST);
    }

    private void startSelectDirectionActivity()
    {
        Intent intent = new Intent();
        intent.setClass(this, BusDirectionSelector.class);
        intent.putExtra(Route.KEY, selectedRoute);
        startActivityForResult(intent, DIRECTION_REQUEST);
    }

    private void startSelectStopActivity()
    {
        Intent intent = new Intent();
        intent.setClass(this, BusStopSelector.class);
        intent.putExtra(Route.KEY, selectedRoute);
        //TODO use better key
        intent.putExtra("DIRECTION", (Parcelable) selectedDirection);
        startActivityForResult(intent, STOP_REQUEST);
    }

    private void startBusPredictionActivity()
    {
        Intent intent = new Intent();
        intent.setClass(this, BusStopPrediction.class);
        intent.putExtra(Stop.KEY, selectedStop);
        startActivity(intent);
    }

    private void setSelectedRoute(String route)
    {
        this.selectedRoute = route;
        this.uiSelectedRoute.setText("Route: " + this.selectedRoute);
    }

    private void setSelectedDirection(Direction direction)
    {
        this.selectedDirection = direction;
        this.uiSelectedDirection.setText("Direction: " + this.selectedDirection);
    }

    private void setSelectedStop(int stop)
    {
        this.selectedStop = stop;
        this.uiSelectedStop.setText("Stop: " + this.selectedStop);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ROUTE_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                String route = data.getStringExtra(BusRouteSelector.RETURN_INTENT_KEY);
                setSelectedRoute(route);
            }
        }
        else if (requestCode == DIRECTION_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                Direction direction = (Direction) data.getSerializableExtra(BusDirectionSelector.RETURN_INTENT_KEY);
                setSelectedDirection(direction);
            }
        }
        else if (requestCode == STOP_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                int stop = data.getIntExtra(BusStopSelector.RETURN_INTENT_KEY, -1);
                setSelectedStop(stop);
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
