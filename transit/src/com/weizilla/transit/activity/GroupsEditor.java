package com.weizilla.transit.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.weizilla.transit.R;
import com.weizilla.transit.data.Group;
import com.weizilla.transit.db.GroupStore;
import com.weizilla.transit.ui.GroupsAdapter;

import java.util.List;

/**
 * Activity for creating, viewing, modifying and deleting groups
 *
 * @author wei
 *         Date: 11/28/13
 *         Time: 12:58 PM
 */
public class GroupsEditor extends Activity implements AdapterView.OnItemLongClickListener
{
    private static final String TAG = "transit.GroupsActivity";
    private GroupStore store;
    private GroupsAdapter groupsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        store = new GroupStore(this);

        initGui();
    }

    private void initGui()
    {
        setContentView(R.layout.groups_editor);
        groupsAdapter = new GroupsAdapter(this);
        ListView uiGroupsDisplay = (ListView) findViewById(R.id.uiGroupList);
        uiGroupsDisplay.setAdapter(groupsAdapter);
//        uiGroupsDisplay.setOnItemClickListener(this);
        uiGroupsDisplay.setLongClickable(true);
        uiGroupsDisplay.setOnItemLongClickListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        store.open();

        refreshGroups();
    }

    @Override
    protected  void onPause()
    {
        super.onPause();
        store.close();
    }

    public void refreshGroups()
    {
        new RefreshGroupsTask().execute();
    }

    private void updateGroups(List<Group> groups)
    {
        groupsAdapter.clear();
        groupsAdapter.addAll(groups);

        //TODO remove req to manually trigger filter
        groupsAdapter.getFilter().filter(null);

        groupsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        Group group = groupsAdapter.getItem(position);
        showContextMenu(group);
        return true;
    }

    private void showContextMenu(final Group group)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(group.getName());
        builder.setItems(new String[]{"Delete"}, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                store.removeGroup(group.getName());
                refreshGroups();
            }
        });
        builder.create().show();
    }

    private class RefreshGroupsTask extends AsyncTask<Void, Void, List<Group>>
    {

        @Override
        protected List<Group> doInBackground(Void... params)
        {
            List<Group> groups = store.getGroups();
            Log.d(TAG, "Got " + groups.size() + " groups");
            return groups;
        }

        @Override
        protected void onPostExecute(List<Group> groups)
        {
            super.onPostExecute(groups);
            updateGroups(groups);
        }

    }
}
