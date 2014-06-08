package com.weizilla.transit.ui;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.weizilla.transit.R;
import com.weizilla.transit.data.Group;

/**
 * adapter for a list of groups
 *
 * @author wei
 *         Date: 11/28/13
 *         Time: 1:19 PM
 */
public class GroupsAdapter extends ListAdapter<Group>
{
    public GroupsAdapter(Context context)
    {
        super(context, R.layout.groups_item);
    }

    @Override
    protected boolean isItemIncluded(Group item, String constraint)
    {
        return true; // no filtering
    }

    @Override
    protected void displayItem(Group item, View view)
    {
        TextView uiName = (TextView) view.findViewById(R.id.uiGroupsItemName);
        uiName.setText(item.getName());
    }
}
