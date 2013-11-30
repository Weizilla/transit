package com.weizilla.transit.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import com.google.common.collect.Lists;
import com.weizilla.transit.data.Group;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.util.SqliteTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * test the group store
 *
 * @author wei
 *         Date: 11/27/13
 *         Time: 5:50 PM
 */
public class GroupStoreTest extends AndroidTestCase
{
    private static final String TEST_GROUP_NAME = "TEST_NAME";
    private static final String TEST_GROUP_NAME_2 = "TEST_NAME_2";
    private static final Stop TEST_STOP = new Stop(123, "STOP_NAME", false);
    private static final Stop TEST_STOP_2 = new Stop(234, "STOP_NAME_2", false);
    private static final Stop TEST_STOP_3 = new Stop(345, "STOP_NAME_3", false);
    private GroupStore store;
    private SqliteDbHelper dbHelper;
    private SQLiteDatabase db;

    public void setUp() throws Exception
    {
        super.setUp();
        store = new GroupStore(getContext());
        store.open();
        dbHelper = store.getDatabaseHelper();
        db = dbHelper.getWritableDatabase();
    }

    public void tearDown() throws Exception
    {
        super.tearDown();
        store.close();
        store.deleteDb();
    }

    public void testPreconditions()
    {
        assertNotNull(store);
        assertNotNull(dbHelper);
        assertNotNull(db);
    }

    public void testAddGroup()
    {
        long newId = store.createGroup(TEST_GROUP_NAME);
        assertTrue(newId != -1);

        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME, 1);

        assertSingleTestResultInDb();
    }

    public void testDuplicateAddedReturnsSameIdWithoutErrors()
    {
        long firstId = store.createGroup(TEST_GROUP_NAME);
        assertTrue(firstId != -1);

        long newId = store.createGroup(TEST_GROUP_NAME);
        assertTrue(newId != -1);
        assertEquals(firstId, newId);

        newId = store.createGroup(TEST_GROUP_NAME);
        assertTrue(newId != -1);
        assertEquals(firstId, newId);

        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME, 1);

        assertSingleTestResultInDb();
    }

    public void testDeleteGroupWithName()
    {
        long newId = store.createGroup(TEST_GROUP_NAME);
        assertTrue(newId != -1);

        assertSingleTestResultInDb();

        boolean deleted = store.removeGroup(TEST_GROUP_NAME);
        assertTrue(deleted);

        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME, 0);
    }

//    public void testDeleteGroupWithId()
//    {
//        long newId = store.createGroup(TEST_GROUP_NAME);
//        assertTrue(newId != -1);
//
//        assertSingleTestResultInDb();
//
//        boolean deleted = store.removeGroup(newId);
//        assertTrue(deleted);
//
//        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME);
//        assertEquals(0, totalRows);
//    }

    public void testRemoveGroupRemovesStops()
    {
        long groupId = store.addStop(TEST_GROUP_NAME, TEST_STOP);
        assertTrue(groupId != -1);
        groupId = store.addStop(TEST_GROUP_NAME, TEST_STOP_2);
        assertTrue(groupId != -1);

        assertSingleTestResultInDb();

        SqliteTestUtils.assertRowCount(db, Group.GroupsStopsDB.TABLE_NAME, 2);

        //TODO test remove by id too
        boolean deleted = store.removeGroup(TEST_GROUP_NAME);
        assertTrue(deleted);

        SqliteTestUtils.assertRowCount(db, Group.GroupsStopsDB.TABLE_NAME, 0);
    }

    public void testAddStopsToGroup()
    {
        long groupId = store.addStop(TEST_GROUP_NAME, TEST_STOP);
        assertTrue(groupId != -1);

        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME, 1);

        assertSingleTestResultInDb();

        SqliteTestUtils.assertRowCount(db,Group.GroupsStopsDB.TABLE_NAME, 1);

        assertGroupStopLink(groupId, TEST_STOP);
    }

    public void testNoDuplicatedStopsAdded()
    {
        long groupId = store.addStop(TEST_GROUP_NAME, TEST_STOP);
        assertTrue(groupId != -1);
        groupId = store.addStop(TEST_GROUP_NAME, TEST_STOP);
        assertTrue(groupId != -1);
        groupId = store.addStop(TEST_GROUP_NAME, TEST_STOP);
        assertTrue(groupId != -1);

        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME, 1);

        assertSingleTestResultInDb();

        SqliteTestUtils.assertRowCount(db, Group.GroupsStopsDB.TABLE_NAME, 1);

        assertGroupStopLink(groupId, TEST_STOP);
    }

    //TODO test foreign key constraint between group id and
    // group id in linked table

    public void testRemoveStopsFromGroup()
    {
        long groupId = store.addStop(TEST_GROUP_NAME, TEST_STOP);
        store.addStop(TEST_GROUP_NAME, TEST_STOP_2);

        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME, 1);
        assertSingleTestResultInDb();

        SqliteTestUtils.assertRowCount(db, Group.GroupsStopsDB.TABLE_NAME, 2);
        assertGroupStopLink(groupId, TEST_STOP);
        assertGroupStopLink(groupId, TEST_STOP_2);

        boolean removed = store.removeStop(TEST_GROUP_NAME, TEST_STOP);
        assertTrue(removed);

        SqliteTestUtils.assertRowCount(db, Group.GroupsStopsDB.TABLE_NAME, 1);
        assertGroupStopLink(groupId, TEST_STOP_2);

        removed = store.removeStop(TEST_GROUP_NAME, TEST_STOP_2);
        assertTrue(removed);

        SqliteTestUtils.assertRowCount(db, Group.GroupsStopsDB.TABLE_NAME, 0);

        assertSingleTestResultInDb();
    }

    public void testGetGroupWithId()
    {
        long groupId = store.addStop(TEST_GROUP_NAME, TEST_STOP);
        assertTrue(groupId != -1);
        store.addStop(TEST_GROUP_NAME, TEST_STOP_2);
        store.addStop(TEST_GROUP_NAME, TEST_STOP_3);
        List<Stop> stops = Lists.newArrayList(TEST_STOP, TEST_STOP_2, TEST_STOP_3);

        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME, 1);
        SqliteTestUtils.assertRowCount(db, Group.GroupsStopsDB.TABLE_NAME, 3);

        Group actualGroup = store.getGroup(groupId);
        assertEquals(groupId, actualGroup.getId());
        assertEquals(TEST_GROUP_NAME, actualGroup.getName());
        assertEquals(stops, actualGroup.getStops());
    }

    public void testGetGroupsWithNoStops()
    {
        long groupId = store.createGroup(TEST_GROUP_NAME);
        assertTrue(groupId != -1);
        long groupId2 = store.createGroup(TEST_GROUP_NAME_2);
        assertTrue(groupId2 != -1);

        SqliteTestUtils.assertRowCount(db, Group.DB.TABLE_NAME, 2);

        List<Group> groups = store.getGroups();
        assertEquals(2, groups.size());

        Group group1 = groups.get(0);
        assertEquals(groupId, group1.getId());
        assertEquals(TEST_GROUP_NAME, group1.getName());
        assertTrue(group1.getStops().isEmpty());

        Group group2 = groups.get(1);
        assertEquals(groupId2, group2.getId());
        assertEquals(TEST_GROUP_NAME_2, group2.getName());
        assertTrue(group2.getStops().isEmpty());
    }

    public void testGetGroupsWithStops()
    {
        long groupId = store.addStop(TEST_GROUP_NAME, TEST_STOP);
        assertTrue(groupId != -1);
        store.addStop(TEST_GROUP_NAME, TEST_STOP_2);
        List<Stop> expected1 = new ArrayList<>();
        Collections.addAll(expected1, TEST_STOP, TEST_STOP_2);
        Collections.sort(expected1);

        long groupId2 = store.addStop(TEST_GROUP_NAME_2, TEST_STOP);
        assertTrue(groupId2 != -1);
        store.addStop(TEST_GROUP_NAME_2, TEST_STOP_2);
        store.addStop(TEST_GROUP_NAME_2, TEST_STOP_3);
        List<Stop> expected2 = new ArrayList<>();
        Collections.addAll(expected2, TEST_STOP,
                TEST_STOP_2, TEST_STOP_3);
        Collections.sort(expected2);

        SqliteTestUtils.assertRowCount(db, Group.GroupsStopsDB.TABLE_NAME, 5);

        List<Group> groups = store.getGroups();
        assertEquals(2, groups.size());
        Collections.sort(groups);

        Group group1 = groups.get(0);
        assertEquals(groupId, group1.getId());
        assertEquals(TEST_GROUP_NAME, group1.getName());
        List<Stop> stops1 = group1.getStops();
        Collections.sort(stops1);
        assertEquals(expected1, stops1);

        Group group2 = groups.get(1);
        assertEquals(groupId2, group2.getId());
        assertEquals(TEST_GROUP_NAME_2, group2.getName());
        List<Stop> stops2 = group2.getStops();
        Collections.sort(stops2);
        assertEquals(expected2, stops2);
    }

    private void assertSingleTestResultInDb()
    {
        String[] cols = {Group.DB._ID, Group.DB.NAME};
        String selection = Group.DB.NAME + " = ?";
        String[] selectionArgs = {TEST_GROUP_NAME};
        Cursor cursor = db.query(Group.DB.TABLE_NAME, cols,
                selection, selectionArgs, null, null, null);
        try
        {
            assertNotNull(cursor);
            assertEquals(1, cursor.getCount());

            cursor.moveToFirst();

            String actualName = cursor.getString(cursor.getColumnIndexOrThrow(Group.DB.NAME));
            assertEquals(TEST_GROUP_NAME, actualName);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    private void assertGroupStopLink(long groupId, Stop stop)
    {
        String stopId = String.valueOf(stop.getId());
        String selection = Group.GroupsStopsDB.GROUP_ID + " = ? AND " +
                Group.GroupsStopsDB.STOP_ID + " = ?";
        String[] selectionArgs = {String.valueOf(groupId), stopId};
        String[] cols = {Group.GroupsStopsDB.GROUP_ID,
                Group.GroupsStopsDB.STOP_ID,
                Group.GroupsStopsDB.STOP_NAME};
        Cursor cursor = db.query(Group.GroupsStopsDB.TABLE_NAME, cols,
                selection, selectionArgs, null, null, null);
        try
        {
            assertNotNull(cursor);
            assertEquals(1, cursor.getCount());

            cursor.moveToFirst();

            int groupIdIndex = cursor.getColumnIndexOrThrow(Group.GroupsStopsDB.GROUP_ID);
            long actualGroupId = cursor.getLong(groupIdIndex);
            assertEquals(groupId, actualGroupId);

            int stopIdIndex = cursor.getColumnIndexOrThrow(Group.GroupsStopsDB.STOP_ID);
            long actualStopId = cursor.getLong(stopIdIndex);
            assertEquals(stop.getId(), actualStopId);

            int stopNameIndex = cursor.getColumnIndexOrThrow(Group.GroupsStopsDB.STOP_NAME);
            String actualStopName = cursor.getString(stopNameIndex);
            assertEquals(stop.getName(), actualStopName);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

}
