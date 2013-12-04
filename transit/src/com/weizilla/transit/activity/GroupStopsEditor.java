package com.weizilla.transit.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.weizilla.transit.R;
import com.weizilla.transit.data.Group;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.data.StopList;
import com.weizilla.transit.dataproviders.CTADataProvider;
import com.weizilla.transit.dataproviders.TransitDataProvider;
import com.weizilla.transit.db.GroupStore;
import com.weizilla.transit.ui.GroupStopsAdapter;

/**
 * Allows editing of stops for a group
 *
 * @author wei
 *         Date: 11/29/13
 *         Time: 1:15 PM
 */
public class GroupStopsEditor extends Activity implements AdapterView.OnItemLongClickListener
{
    private static final String TAG = "transit.GroupStopsEditor";
    private static final int BUS_PICKER_RESULT = 0;
    private GroupStopsAdapter adapter;
    private TextView uiGroupName;
    private GroupStore store;
    private Group group; //TODO only store group id?
    private TransitDataProvider dataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        store = new GroupStore(this);
        dataProvider = getDataProvider();

        initGui();

        Intent intent = getIntent();
        Group group = intent.getParcelableExtra(Group.INTENT_KEY);
        setSelectedGroup(group);
    }

    private TransitDataProvider getDataProvider()
    {
        String ctaApiKey = getString(R.string.ctaApiKey);
        Intent intent = getIntent();
        TransitDataProvider dataProvider =
                (TransitDataProvider) intent.getSerializableExtra(TransitDataProvider.KEY);
        return dataProvider != null ? dataProvider : new CTADataProvider(ctaApiKey);
    }

    private void initGui()
    {
        setContentView(R.layout.group_stops_editor);
        adapter = new GroupStopsAdapter(this);

        uiGroupName = (TextView) findViewById(R.id.uiGroupName);

        ListView uiGroupStopDisplay = (ListView) findViewById(R.id.uiGroupStopsDisplay);
        uiGroupStopDisplay.setAdapter(adapter);
        uiGroupStopDisplay.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        final Stop stop = adapter.getItem(position);
        showContextMenu(stop);
        return true;
    }

    private void showContextMenu(final Stop stop)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(stop.getName());
        builder.setItems(new String[]{"Remove"}, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                store.removeStop(group.getId(), stop);
                refreshGroup();
            }
        });
        builder.create().show();
    }

    private void setSelectedGroup(Group group)
    {
        if (group == null)
        {
            return;
        }
        this.group = group;
        uiGroupName.setText(group.getName());
        adapter.clear();
        adapter.addAll(group.getStops());
        adapter.getFilter().filter(null); // TODO remove this requirement
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        store.open();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        store.close();
    }

    public void addStop(View view)
    {
        Intent intent = new Intent();
        intent.setClass(this, BusStopPicker.class);
        intent.putExtra(TransitDataProvider.KEY, dataProvider);
        startActivityForResult(intent, BUS_PICKER_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == BUS_PICKER_RESULT && resultCode == Activity.RESULT_OK)
        {
            //TODO seems ugly to put this here but this is called
            // before onResume() which opens the store
            store.open();

            StopList stopList = data.getParcelableExtra(StopList.INTENT_KEY);
            store.addStop(group.getId(), stopList.getFirstStop());
            refreshGroup();
        }
    }

    private void refreshGroup()
    {
        new RefreshGroupTask().execute();
    }

    private class RefreshGroupTask extends AsyncTask<Void, Void, Group>
    {
        @Override
        protected Group doInBackground(Void ... params)
        {
            return store.getGroup(group.getId());
        }

        @Override
        protected void onPostExecute(Group group)
        {
            super.onPostExecute(group);
            setSelectedGroup(group);
        }
    }
}
