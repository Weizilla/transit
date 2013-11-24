package com.weizilla.transit.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.weizilla.transit.R;
import com.weizilla.transit.activity.BusRouteSelector;
import com.weizilla.transit.data.Route;

import java.util.List;

/**
 * adapter for bus route picker list
 *
 * @author wei
 *         Date: 9/9/13
 *         Time: 12:09 PM
 */
public class BusRouteAdapter extends ArrayAdapter<Route>
{
    private List<Route> routes;

    public BusRouteAdapter(Context context, List<Route> routes)
    {
        super(context, R.layout.bus_route_item, routes);
        this.routes = routes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.bus_route_item, parent, false);
        }
        else
        {
            view = convertView;
        }

        Route route = routes.get(position);

        TextView uiIsFav = (TextView) view.findViewById(R.id.uiBusRouteItemIsFav);
        int background = route.isFavorite() ? BusRouteSelector.FAV_BACKGROUND_COLOR : Color.TRANSPARENT;
        uiIsFav.setBackgroundColor(background);

        TextView uiId = (TextView) view.findViewById(R.id.uiBusRouteItemId);
        uiId.setText(route.getId());

        TextView uiName = (TextView) view.findViewById(R.id.uiBusRouteItemName);
        uiName.setText(route.getName());

        return view;
    }

}
