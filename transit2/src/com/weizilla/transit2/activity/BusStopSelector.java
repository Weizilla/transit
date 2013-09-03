package com.weizilla.transit2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.weizilla.transit2.R;

/**
 * launches child activites for selecting route attributes and displays the currently selected ones
 *
 * @author wei
 *         Date: 9/2/13
 *         Time: 7:07 PM
 */
public class BusStopSelector extends Activity
{
    private TextView uiSelectedRoute;

    private static final int BUS_ROUTE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.bus_stop_select);

        this.uiSelectedRoute = (TextView) findViewById(R.id.uiSelectedRoute);
    }

    public void selectStopStep(View view)
    {
        startBusRouteSelectActivity();
    }

    private void startBusRouteSelectActivity()
    {
        Intent intent = new Intent();
        intent.setClass(this, BusRouteSelector.class);
        startActivityForResult(intent, BUS_ROUTE_REQUEST);
    }

    private void setSelectedRoute(String route)
    {
        this.uiSelectedRoute.setText("Route: " + route);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BUS_ROUTE_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                String route = data.getStringExtra(BusRouteSelector.RETURN_INTENT_KEY);
                setSelectedRoute(route);
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
