package com.weizilla.transit.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import com.weizilla.transit.data.Group;

/**
 * test the group store
 *
 * @author wei
 *         Date: 11/27/13
 *         Time: 5:50 PM
 */
public class GroupStoreTest extends AndroidTestCase
{
    private static final String TEST_NAME = "TEST_NAME";
    private static final Group TEST_GROUP = new Group(TEST_NAME);
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
        dbHelper.close();
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
        long newId = store.addGroup(TEST_GROUP);
        assertTrue(newId != -1);

        int totalRows = SqliteUtils.countRows(db, Group.DB.TABLE_NAME);
        assertEquals(1, totalRows);

        assertSingleTestResultInDb();
    }

    public void testDuplicateAddedReturnsSameIdWithoutErrors()
    {
        long firstId = store.addGroup(TEST_GROUP);
        assertTrue(firstId != -1);

        long newId = store.addGroup(TEST_GROUP);
        assertTrue(newId != -1);
        assertEquals(firstId, newId);

        newId = store.addGroup(TEST_GROUP);
        assertTrue(newId != -1);
        assertEquals(firstId, newId);

        int totalRows = SqliteUtils.countRows(db, Group.DB.TABLE_NAME);
        assertEquals(1, totalRows);

        assertSingleTestResultInDb();
    }

    public void testDeleteGroup()
    {
        //TODO
    }

    public void testRemoveGroupRemovesStops()
    {
        //TODO
    }

    public void testAddStopsToGroup()
    {
        //TODO
    }

    public void testAddStopsToNonExistentGroupWithoutErrors()
    {
        //TODO
    }

    public void testRemoveStopsFromGroup()
    {
        //TODO
    }

    private void assertSingleTestResultInDb()
    {
        String[] cols = {Group.DB._ID, Group.DB.NAME};
        String selection = Group.DB.NAME + " = ?";
        String[] selectionArgs = {TEST_NAME};
        Cursor cursor = db.query(Group.DB.TABLE_NAME, cols,
                selection, selectionArgs, null, null, null);

        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());

        cursor.moveToFirst();

        String actualName = cursor.getString(cursor.getColumnIndexOrThrow(Group.DB.NAME));
        assertEquals(TEST_NAME, actualName);
    }

}
