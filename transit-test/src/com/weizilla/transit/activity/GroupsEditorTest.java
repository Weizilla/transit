package com.weizilla.transit.activity;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.TextView;
import com.jayway.android.robotium.solo.Solo;
import com.weizilla.transit.R;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.db.GroupStore;
import com.weizilla.transit.util.TestUtils;

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
    private static final Stop TEST_STOP_1 = new Stop(123, "TEST_STOP_1", false);
    private static final Stop TEST_STOP_2 = new Stop(456, "TEST_STOP_2", false);
    private static final String TEST_IDS = TEST_STOP_1.getId() + " " + TEST_STOP_2.getId();
    private static final long TEST_GROUP_ID = 100;
    private static final long TEST_GROUP_ID_2 = 200;
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

        TestUtils.setMockTransitDataProvider(getInstrumentation());

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
        boolean added = store.createGroup(TEST_GROUP_ID, TEST_GROUP_NAME);
        assertTrue(added);
        activity.refreshGroups();
        solo.waitForView(R.id.uiGroupList);

        String name = clickInList(1);
        assertEquals(TEST_GROUP_NAME, name);
    }

    public void testDeleteFirstGroup()
    {
        addGroup(TEST_GROUP_ID, TEST_GROUP_NAME, TEST_STOP_1, TEST_STOP_2);
        addGroup(TEST_GROUP_ID_2, TEST_GROUP_NAME_2, TEST_STOP_1, TEST_STOP_2);
        activity.refreshGroups();

        solo.waitForView(R.id.uiGroupList);

        String name = clickLongInList(1);
        assertEquals(TEST_GROUP_NAME, name);

        solo.waitForDialogToOpen(TIMEOUT);
        solo.clickInList(2);

        solo.waitForView(R.id.uiGroupList);

        name = clickInList(1);
        assertEquals(TEST_GROUP_NAME_2, name);
    }

    public void testCreateGroupByDialog()
    {
        String buttonString = solo.getString(R.string.new_group);
        solo.clickOnButton(buttonString);
        solo.waitForDialogToOpen(TIMEOUT);
        solo.enterText(0, TEST_GROUP_NAME);
        buttonString = solo.getString(R.string.create);
        solo.clickOnButton(buttonString);

        solo.waitForView(R.id.uiGroupList);
        String name = clickInList(1);
        assertEquals(TEST_GROUP_NAME, name);
    }

    public void testClickOnGroupOpensPredictionsWithStopId()
    {
        addGroup(TEST_GROUP_ID, TEST_GROUP_NAME, TEST_STOP_1, TEST_STOP_2);

        activity.refreshGroups();
        solo.waitForView(R.id.uiGroupList);

        String name = clickInList(1);
        assertEquals(TEST_GROUP_NAME, name);

        solo.waitForView(R.id.uiBusPredStopIdInput);

        EditText stopIdInput = solo.getEditText(0);
        assertEquals(TEST_IDS, stopIdInput.getText().toString());
    }

    public void testEditGroupByContextMenu()
    {
        addGroup(TEST_GROUP_ID, TEST_GROUP_NAME, TEST_STOP_1, TEST_STOP_2);

        activity.refreshGroups();
        solo.waitForView(R.id.uiGroupList);

        String name = clickLongInList(0);
        assertEquals(TEST_GROUP_NAME, name);

        solo.waitForDialogToOpen(TIMEOUT);
        solo.clickInList(1);

        solo.waitForView(R.id.uiGroupName);

        TextView groupName = solo.getText(1);
        assertEquals(TEST_GROUP_NAME, groupName.getText().toString());
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

    private void addGroup(long groupId, String groupName, Stop ... stops)
    {
        store.createGroup(groupId, groupName);
        for (Stop stop : stops)
        {
            store.addStop(groupId, stop);
        }
    }

}
