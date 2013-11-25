package com.weizilla.transit.db;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

/**
 * Tests the favorite stop store
 *
 * @author wei
 *         Date: 11/24/13
 *         Time: 6:18 PM
 */
public class FavStopStoreTest extends AndroidTestCase
{
    private static final String DB_TEST_PREFIX = "test_";
    private SqliteDbHelper helper;
    private FavStopStore store;
    private SQLiteDatabase db;

    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), DB_TEST_PREFIX);
        store = new FavStopStore(context);
        store.open();

        helper = store.getDatabaseHelper();
        db = helper.getWritableDatabase();
    }

    @Override
    public void tearDown() throws Exception
    {
        super.tearDown();
        store.close();
    }

    public void testPreconditions()
    {
        assertNotNull(store);
        assertNotNull(helper);
        assertNotNull(db);
    }
}
