package com.weizilla.transit.db;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import com.weizilla.transit.data.FavRoute;

/**
 * Tests favorite route store CRUD operations
 * TODO switch to ProvderTestCase2
 * @author wei
 *         Date: 11/22/13
 *         Time: 9:58 AM
 */
public class FavRouteStoreTest extends AndroidTestCase
{
    private static final String TAG = "FavRouteStoreTest";
    private static final String DB_TEST_PREFIX = "test_";
    private FavRouteStore.DatabaseHelper helper;
    private FavRouteStore store;

    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), DB_TEST_PREFIX);
        store = new FavRouteStore(context);
        store.open();

        helper = store.getDatabaseHelper();
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
    }

    public void testDatabaseCreated()
    {
        assertNotNull(helper.getWritableDatabase());
    }

    public void testTableCreated()
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        int numTables = SqliteUtils.countTables(db, FavRoute.DB.TABLE_NAME);
        assertEquals(1, numTables);
    }
}
