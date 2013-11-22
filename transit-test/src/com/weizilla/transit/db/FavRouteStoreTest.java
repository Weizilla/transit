package com.weizilla.transit.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import com.google.common.collect.Lists;
import com.weizilla.transit.data.FavRoute;
import com.weizilla.transit.data.Route;

import java.util.Collections;
import java.util.List;

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
    private static final String TEST_NAME = "TEST_NAME";
    private static final String TEST_ID = "TEST_ID";
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

    public void testFavoriteIsAdded()
    {
        Route route = new Route(TEST_ID, TEST_NAME);
        long newId = store.addRoute(route);
        assertTrue(newId != -1);

        SQLiteDatabase db = helper.getReadableDatabase();

        int totalRows = SqliteUtils.countRows(db, FavRoute.DB.TABLE_NAME);
        assertEquals(1, totalRows);

        String[] cols = {FavRoute.DB.ID, FavRoute.DB.NAME};
        String selection = FavRoute.DB.ID + " = ?";
        String[] selectionArgs = {TEST_ID};
        Cursor cursor = db.query(FavRoute.DB.TABLE_NAME, cols,
                selection, selectionArgs, null, null, null);

        assertSingleTestResult(cursor);
    }

    public void testDuplicateAddedWithoutErrors() throws Exception
    {
        Route route = new Route(TEST_ID, TEST_NAME);
        long newId = store.addRoute(route);
        assertTrue(newId != -1);
        newId = store.addRoute(route);
        assertTrue(newId != -1);
        newId = store.addRoute(route);
        assertTrue(newId != -1);

        SQLiteDatabase db = helper.getReadableDatabase();

        int totalRows = SqliteUtils.countRows(db, FavRoute.DB.TABLE_NAME);
        assertEquals(1, totalRows);

        String[] cols = {FavRoute.DB.ID, FavRoute.DB.NAME};
        String selection = FavRoute.DB.ID + " = ?";
        String[] selectionArgs = {TEST_ID};
        Cursor cursor = db.query(FavRoute.DB.TABLE_NAME, cols,
                selection, selectionArgs, null, null, null);

        assertSingleTestResult(cursor);
    }


    private void assertSingleTestResult(Cursor cursor)
    {
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());

        cursor.moveToFirst();

        String actualId = cursor.getString(cursor.getColumnIndexOrThrow(FavRoute.DB.ID));
        assertEquals(TEST_ID, actualId);
        String actualName = cursor.getString(cursor.getColumnIndexOrThrow(FavRoute.DB.NAME));
        assertEquals(TEST_NAME, actualName);

    }

    public void testGetAllFavRoutes() throws Exception
    {
        Route routeA = new Route(TEST_ID + "_A", TEST_NAME + "_A");
        Route routeB = new Route(TEST_ID + "_B", TEST_NAME + "_B");
        Route routeC = new Route(TEST_ID + "_C", TEST_NAME + "_C");
        Route routeD = new Route(TEST_ID + "_D", TEST_NAME + "_D");
        List<Route> allRoutes = Lists.newArrayList(routeA, routeB, routeC, routeD);
        Collections.sort(allRoutes);

        store.addRoute(routeA);
        store.addRoute(routeB);
        store.addRoute(routeC);
        store.addRoute(routeD);

        List<Route> actualRoutes = store.getRoutes();
        assertEquals(allRoutes, actualRoutes);
    }

    public void testGetAllFavRoutesSorted() throws Exception
    {
        Route routeA = new Route(TEST_ID + "_A", TEST_NAME + "_A");
        Route routeB = new Route(TEST_ID + "_B", TEST_NAME + "_B");
        Route routeC = new Route(TEST_ID + "_C", TEST_NAME + "_C");
        List<Route> allRoutes = Lists.newArrayList(routeA, routeB, routeC);
        Collections.sort(allRoutes);

        store.addRoute(routeC);
        store.addRoute(routeB);
        store.addRoute(routeA);

        List<Route> actualRoutes = store.getRoutes();
        assertEquals(allRoutes, actualRoutes);
    }

}
