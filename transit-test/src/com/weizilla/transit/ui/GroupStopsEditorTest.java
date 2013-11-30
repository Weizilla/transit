package com.weizilla.transit.ui;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import com.jayway.android.robotium.solo.Solo;
import com.weizilla.transit.R;
import com.weizilla.transit.activity.GroupStopsEditor;
import com.weizilla.transit.data.Group;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.dataproviders.MockTransitDataProvider;
import com.weizilla.transit.dataproviders.TransitDataProvider;

import java.text.ParseException;
import java.util.List;

/**
 * tests the group stops editor activity
 *
 * @author wei
 *         Date: 11/29/13
 *         Time: 1:47 PM
 */
public class GroupStopsEditorTest extends ActivityInstrumentationTestCase2<GroupStopsEditor>
{
    private static final String TEST_GROUP_NAME = "TEST GROUP NAME";
    private static final Stop TEST_STOP = new Stop(123, "TEST_STOP_NAME", false);
    private static final Stop TEST_STOP_2 = new Stop(234, "TEST_STOP_NAME_2", false);
    private Solo solo;

    public GroupStopsEditorTest()
    {
        super(GroupStopsEditor.class);
    }

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception
    {
        if (solo != null)
        {
            solo.finishOpenedActivities();
        }
        super.tearDown();
    }

    public void testGroupPopulates() throws ParseException
    {
        Intent intent = new Intent();
        intent.putExtra(TransitDataProvider.KEY, new MockTransitDataProvider());
        intent.putExtra(Group.INTENT_KEY, createTestGroup());
        setActivityIntent(intent);

        GroupStopsEditor activity = getActivity();
        solo = new Solo(getInstrumentation(), activity);

        solo.waitForView(R.id.uiGroupName);
        TextView text = solo.getText(1);
        assertEquals(TEST_GROUP_NAME, text.getText());

        String stop1 = clickInList(1);
        assertEquals(stop1, TEST_STOP.getName());

        String stop2 = clickInList(2);
        assertEquals(stop2, TEST_STOP_2.getName());
    }

    private String clickInList(int line)
    {
        solo.scrollToTop();
        List<TextView> views = solo.clickInList(line);
        return views.get(0).getText().toString();
    }

    private static Group createTestGroup()
    {
        Group group = new Group(0, TEST_GROUP_NAME);
        group.addStop(TEST_STOP);
        group.addStop(TEST_STOP_2);
        return group;
    }

}
