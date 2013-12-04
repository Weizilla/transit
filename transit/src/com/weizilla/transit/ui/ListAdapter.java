package com.weizilla.transit.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * generic adapter for list views with dynamic
 * adding/removing and filtering
 *
 * @author wei
 *         Date: 11/26/13
 *         Time: 4:34 PM
 */
public abstract class ListAdapter<T extends Comparable<T> > extends BaseAdapter implements Filterable
{
    protected String TAG = "transit.ListAdapter[" + this.getClass().getSimpleName() + "]";
    protected LayoutInflater inflater;
    protected Filter filter;
    protected List<T> displayItems;
    protected List<T> allItems;
    protected int itemUiResource;

    public ListAdapter(Context context, int itemUiResource)
    {
        this.itemUiResource = itemUiResource;
        inflater = LayoutInflater.from(context);
        displayItems = new ArrayList<>();

        //TODO use an lock obj like ArrayAdapter does?
        allItems = new CopyOnWriteArrayList<>();
        filter = buildFilter();
    }

    protected abstract boolean isItemIncluded(T item, String constraint);

    protected abstract void displayItem(T item, View view);

    private Filter buildFilter()
    {
        return new Filter()
        {

            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                List<T> filteredItems;
                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0)
                {
                    filteredItems = new ArrayList<>(allItems);
                }
                else
                {
                    filteredItems = filterItems(constraint.toString());
                }
                filterResults.values = filteredItems;
                filterResults.count = filteredItems.size();

                Log.d(TAG, "Filtered result count: " + filterResults.count);

                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                displayItems = (ArrayList<T>) results.values;
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

    protected List<T> filterItems(String constraint)
    {
        List<T> filtered = new ArrayList<>();
        for (T item : allItems)
        {
            if (isItemIncluded(item, constraint))
            {
                filtered.add(item);
            }
        }

        return filtered;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;

        if (view == null)
        {
            view = inflater.inflate(itemUiResource, parent, false);
        }

        T item = getItem(position);

        if (item != null)
        {
            displayItem(item, view);
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
        return displayItems.size();
    }

    @Override
    public T getItem(int position)
    {
        return displayItems.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public void addAll(Collection<? extends T> items)
    {
        Log.d(TAG, "Adding " + items.size() + " items");
        List<? extends T> sorted = new ArrayList<>(items);
        Collections.sort(sorted);
        allItems.addAll(sorted);
    }

    public void clear()
    {
        Log.d(TAG, "Clearing all items");
        allItems.clear();
    }
}
