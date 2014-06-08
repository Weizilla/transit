package com.weizilla.transit.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import com.weizilla.transit.R;
import com.weizilla.transit.activity.BusRouteSelector;
import com.weizilla.transit.data.Route;

/**
 * adapter for bus route picker list
 *
 * @author wei
 *         Date: 9/9/13
 *         Time: 12:09 PM
 */
public class BusRouteAdapter extends ListAdapter<Route>
{
    private static final String TAG = "transit.BusRouteAdapter";

    public BusRouteAdapter(Context context)
    {
        super(context, R.layout.bus_route_item);
    }

    @Override
    protected boolean isItemIncluded(Route route, String constraint)
    {
        String lower = constraint.toLowerCase();
        return (route.getId().toLowerCase().contains(lower)
                || route.getName().toLowerCase().contains(lower));
    }

    @Override
    protected void displayItem(Route route, View view)
    {
        TextView uiIsFav = (TextView) view.findViewById(R.id.uiBusRouteItemIsFav);
        int background = route.isFavorite() ? BusRouteSelector.FAV_BACKGROUND_COLOR : Color.TRANSPARENT;
        uiIsFav.setBackgroundColor(background);

        TextView uiId = (TextView) view.findViewById(R.id.uiBusRouteItemId);
        uiId.setText(route.getId());

        TextView uiName = (TextView) view.findViewById(R.id.uiBusRouteItemName);
        uiName.setText(route.getName());
    }
}
