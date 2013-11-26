package com.weizilla.transit.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.weizilla.transit.R;
import com.weizilla.transit.activity.BusRouteSelector;
import com.weizilla.transit.data.Route;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * adapter for bus route picker list
 *
 * @author wei
 *         Date: 9/9/13
 *         Time: 12:09 PM
 */
public class BusRouteAdapter extends BaseAdapter implements Filterable
{
    private static final String TAG = "BusRouteAdpater";
    private LayoutInflater inflater;
    private Filter filter;
    private List<Route> displayRoutes;
    private List<Route> allRoutes;

    public BusRouteAdapter(Context context)
    {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        displayRoutes = new ArrayList<>();
        allRoutes = new CopyOnWriteArrayList<>();
        filter = buildFilter();
    }

    private Filter buildFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0)
                {
                    List<Route> results = new ArrayList<>(allRoutes);
                    filterResults.values = results;
                    filterResults.count = results.size();
                }
                else
                {
                    List<Route> filtered = filterRoutes(constraint.toString());
                    filterResults.values = filtered;
                    filterResults.count = filtered.size();
                }
                Log.d(TAG, "Num of filtered results: " + filterResults.count);
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                displayRoutes = (ArrayList<Route>) results.values;
                if (results.count > 0)
                {
                    notifyDataSetChanged();
                }
                else
                {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    private List<Route> filterRoutes(String constraint)
    {
        List<Route> filtered = new ArrayList<>();
        for (Route route : allRoutes)
        {
            String lower = constraint.toLowerCase();
            if (route.getId().toLowerCase().contains(lower)
                    || route.getName().toLowerCase().contains(lower)) {

                filtered.add(route);
            }
        }
        return filtered;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;

        if (convertView == null)
        {
            view = inflater.inflate(R.layout.bus_route_item, parent, false);
        }
        else
        {
            view = convertView;
        }

        Route route = getItem(position);

        if (route != null)
        {
            TextView uiIsFav = (TextView) view.findViewById(R.id.uiBusRouteItemIsFav);
            int background = route.isFavorite() ? BusRouteSelector.FAV_BACKGROUND_COLOR : Color.TRANSPARENT;
            uiIsFav.setBackgroundColor(background);

            TextView uiId = (TextView) view.findViewById(R.id.uiBusRouteItemId);
            uiId.setText(route.getId());

            TextView uiName = (TextView) view.findViewById(R.id.uiBusRouteItemName);
            uiName.setText(route.getName());
        }

        return view;
    }

    @Override
    public Filter getFilter()
    {
        return filter;
    }

    @Override
    public int getCount()
    {
        return displayRoutes.size();
    }

    @Override
    public Route getItem(int position)
    {
        return displayRoutes.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public void addAll(Collection<? extends Route> collection)
    {
        Log.d(TAG, "Adding " + collection.size() + " routes to adapter");
        allRoutes.addAll(collection);
    }

    public void clear()
    {
        Log.d(TAG, "Clearing adapter");
        allRoutes.clear();
    }
}
