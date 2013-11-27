package com.weizilla.transit.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import com.weizilla.transit.R;
import com.weizilla.transit.activity.BusStopSelector;
import com.weizilla.transit.data.Stop;

/**
 * list adapter for bus stop
 *
 * @author wei
 *         Date: 9/9/13
 *         Time: 3:37 PM
 */
public class BusStopAdapter extends ListAdapter<Stop>
{
    private static final String TAG = "transit.BusStopAdapter";

    public BusStopAdapter(Context context)
    {
        super(context, R.layout.bus_stop_item);
    }

    @Override
    protected boolean isItemIncluded(Stop stop, String constraint)
    {
        String lower = constraint.toLowerCase();
        return stop.getName().toLowerCase().contains(lower);
    }

    @Override
    protected void displayItem(Stop stop, View view)
    {
        int background = stop.isFavorite() ? BusStopSelector.FAV_BACKGROUND_COLOR : Color.TRANSPARENT;
        TextView uiIsFav = (TextView) view.findViewById(R.id.uiBusStopItemIsFav);
        uiIsFav.setBackgroundColor(background);

        TextView uiName = (TextView) view.findViewById(R.id.uiBusStopItemName);
        uiName.setText(stop.getName());
    }
}
