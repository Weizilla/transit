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
import com.weizilla.transit.data.StopList;

/**
 * launches activites to pick and return a bus stop
 *
 * @author wei
 *         Date: 9/2/13
 *         Time: 7:07 PM
 */
public class BusStopPicker extends TransitActivity
{
    private static final String TAG = "transit.BusStopPicker";
    private static final int ROUTE_REQUEST = 1;
    private static final int DIRECTION_REQUEST = 2;
    private static final int STOP_REQUEST = 3;

    private TextView uiSelectedRoute;
    private TextView uiSelectedDirection;
    private Route selectedRoute;
    private Direction selectedDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bus_stop_picker);
        uiSelectedRoute = (TextView) findViewById(R.id.uiSelectedRoute);
        uiSelectedDirection = (TextView) findViewById(R.id.uiSelectedDirection);

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
        else
        {
            startSelectStopActivity();
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
        intent.putExtra(Direction.KEY, (Parcelable) selectedDirection);
        startActivityForResult(intent, STOP_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == ROUTE_REQUEST && resultCode == RESULT_OK)
        {
            Route route = data.getParcelableExtra(Route.KEY);
            setSelectedRoute(route);
            startNextStep(null);
        }
        else if (requestCode == DIRECTION_REQUEST && resultCode == RESULT_OK)
        {
            Direction direction = data.getParcelableExtra(Direction.KEY);
            setSelectedDirection(direction);
            startNextStep(null);
        }
        else if (requestCode == STOP_REQUEST && resultCode == RESULT_OK)
        {
            Stop stop = data.getParcelableExtra(Stop.KEY);
            finishWithStop(stop);
        }
    }

    private void setSelectedRoute(Route route)
    {
        selectedRoute = route;
        uiSelectedRoute.setText("Route: "
                + selectedRoute.getId() + " - " + selectedRoute.getName());
    }

    private void setSelectedDirection(Direction direction)
    {
        selectedDirection = direction;
        uiSelectedDirection.setText("Direction: "
                + selectedDirection);
    }

    private void finishWithStop(Stop stop)
    {
        Intent intent = new Intent();
        intent.putExtra(StopList.KEY, new StopList(stop));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void startOver(View view)
    {
        selectedRoute = null;
        uiSelectedRoute.setText("Route:");
        selectedDirection = null;
        uiSelectedDirection.setText("Direction:");
    }
}
