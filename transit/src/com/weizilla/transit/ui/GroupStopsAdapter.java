package com.weizilla.transit.ui;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.weizilla.transit.R;
import com.weizilla.transit.data.Stop;

/**
 * Displays the stops for a group
 *
 * @author wei
 *         Date: 11/29/13
 *         Time: 6:48 PM
 */
public class GroupStopsAdapter extends ListAdapter<Stop>
{
    public GroupStopsAdapter(Context context)
    {
        super(context, R.layout.group_stops_item);
    }

    @Override
    protected boolean isItemIncluded(Stop item, String constraint)
    {
        return true;
    }

    @Override
    protected void displayItem(Stop item, View view)
    {
        TextView uiStopName = (TextView) view.findViewById(R.id.uiGroupStopsItemName);
        uiStopName.setText(item.getName());
    }
}
