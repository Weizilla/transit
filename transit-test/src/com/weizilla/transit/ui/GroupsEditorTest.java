package com.weizilla.transit.ui;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import com.jayway.android.robotium.solo.Solo;
import com.weizilla.transit.R;
import com.weizilla.transit.activity.GroupsEditor;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.db.GroupStore;

import java.util.List;

/**
 * Tests the Groups editor
 *
 * @author wei
 *         Date: 11/28/13
 *         Time: 8:33 PM
 */
public class GroupsEditorTest extends ActivityInstrumentationTestCase2<GroupsEditor>
{
    private static final String TEST_GROUP_NAME = "TEST_GROUP_NAME";
    private static final String TEST_GROUP_NAME_2 = "TEST_GROUP_NAME_2";
    private static final int TIMEOUT = 1000;
    private GroupsEditor activity;
    private Solo solo;
    private GroupStore store;

    public GroupsEditorTest()
    {
        super(GroupsEditor.class);
    }

    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        activity = getActivity();
        solo = new Solo(getInstrumentation(), activity);
        store = new GroupStore(activity);
        store.open();

        solo.waitForView(R.id.uiGroupList);
    }

    @Override
    public void tearDown() throws Exception
    {
        solo.finishOpenedActivities();
        store.close();
        store.deleteDb();
        super.tearDown();
    }

    public void testGroupsPopulatedByDb()
    {
        addGroup(TEST_GROUP_NAME, 123, 234);

        activity.refreshGroups();
        solo.waitForView(R.id.uiGroupList);

        String name = clickInList(1);
        assertEquals(TEST_GROUP_NAME, name);
    }

    public void testDeleteFirstGroup()
    {
        addGroup(TEST_GROUP_NAME, 123, 234);
        addGroup(TEST_GROUP_NAME_2, 123, 234);
        activity.refreshGroups();

        solo.waitForView(R.id.uiGroupList);

        String name = clickLongInList(1);
        assertEquals(TEST_GROUP_NAME, name);

        solo.waitForDialogToOpen(TIMEOUT);
        solo.clickInList(1);

        solo.waitForView(R.id.uiGroupList);

        name = clickInList(1);
        assertEquals(TEST_GROUP_NAME_2, name);
    }

    private String clickInList(int line)
    {
        solo.scrollToTop();
        List<TextView> views = solo.clickInList(line, 0);
        TextView textView = views.get(0);
        return textView.getText().toString();
    }

    private String clickLongInList(int line)
    {
        solo.scrollToTop();
        List<TextView> views = solo.clickLongInList(line, 0);
        TextView textView = views.get(0);
        return textView.getText().toString();
    }

    private void addGroup(String groupName, int ... stopIds)
    {
        for (int stopId : stopIds)
        {
            //TODO add name too?
            Stop stop = new Stop(stopId, null, false);
            store.addStop(groupName, stop);
        }
    }

}
