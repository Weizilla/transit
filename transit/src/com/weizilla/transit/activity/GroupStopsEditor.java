package com.weizilla.transit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import com.weizilla.transit.R;
import com.weizilla.transit.data.Group;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.db.GroupStore;
import com.weizilla.transit.ui.GroupStopsAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Allows editing of stops for a group
 *
 * @author wei
 *         Date: 11/29/13
 *         Time: 1:15 PM
 */
public class GroupStopsEditor extends Activity
{
    private static final String TAG = "transit.GroupStopsEditor";
    private GroupStopsAdapter adapter;
    private TextView uiGroupName;
    private GroupStore store;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        store = new GroupStore(this);

        initGui();

        Intent intent = getIntent();
        group = intent.getParcelableExtra(Group.INTENT_KEY);

        refreshUi();
    }

    private void initGui()
    {
        setContentView(R.layout.group_stops_editor);
        adapter = new GroupStopsAdapter(this);

        uiGroupName = (TextView) findViewById(R.id.uiGroupName);

        ListView uiGroupStopDisplay = (ListView) findViewById(R.id.uiGroupStopsDisplay);
        uiGroupStopDisplay.setAdapter(adapter);
    }

    public void refresh()
    {
        if (group != null)
        {
            new RefreshGroupTask().execute(group);
        }
    }

    private void refreshUi()
    {
        String name = group != null ? group.getName() : "No Group";
        List<Stop> stops = group != null ? group.getStops() : Collections.<Stop>emptyList();

        uiGroupName.setText(name);
        adapter.clear();
        adapter.addAll(stops);
        adapter.getFilter().filter(null); // TODO remove this requirement
        adapter.notifyDataSetChanged();
    }

    private class RefreshGroupTask extends AsyncTask<Group, Void, Group>
    {
        @Override
        protected Group doInBackground(Group ... params)
        {
            return store.getGroup(params[0].getId());
        }

        @Override
        protected void onPostExecute(Group group)
        {
            super.onPostExecute(group);
            GroupStopsEditor.this.group = group;
            refreshUi();
        }
    }
}
