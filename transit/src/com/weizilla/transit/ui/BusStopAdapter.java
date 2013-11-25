package com.weizilla.transit.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.weizilla.transit.R;
import com.weizilla.transit.activity.BusStopSelector;
import com.weizilla.transit.data.Stop;

import java.util.List;

/**
 * list adapter for bus stop
 *
 * @author wei
 *         Date: 9/9/13
 *         Time: 3:37 PM
 */
public class BusStopAdapter extends ArrayAdapter<Stop>
{
    private List<Stop> stops;

    public BusStopAdapter(Context context, List<Stop> stops)
    {
        super(context, R.layout.bus_stop_item, stops);
        this.stops = stops;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.bus_stop_item, parent, false);
        }
        else
        {
            view = convertView;
        }

        Stop stop = stops.get(position);

        int background = stop.isFavorite() ? BusStopSelector.FAV_BACKGROUND_COLOR : Color.TRANSPARENT;
        TextView uiIsFav = (TextView) view.findViewById(R.id.uiBusStopItemIsFav);
        uiIsFav.setBackgroundColor(background);

        TextView uiName = (TextView) view.findViewById(R.id.uiBusStopItemName);
        uiName.setText(stop.getName());

        return view;
    }
}
