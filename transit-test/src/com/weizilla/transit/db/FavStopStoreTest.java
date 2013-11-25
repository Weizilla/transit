package com.weizilla.transit.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import com.google.common.collect.Lists;
import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.FavStop;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;

import java.util.List;

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
    private static final Route TEST_ROUTE
            = new Route("TEST_ROUTE_ID", "TEST_ROUTE_NAME", false);
    private static final Route TEST_ROUTE_2
            = new Route("TEST_ROUTE_ID_2", "TEST_ROUTE_NAME_2", false);
    private static final Direction TEST_DIR_E = Direction.Eastbound;
    private static final Direction TEST_DIR_W = Direction.Westbound;
    private static final int TEST_ID = 123;
    private static final String TEST_NAME = "TEST_STOP_NAME";
    private static final Stop TEST_STOP = new Stop(TEST_ID, TEST_NAME);
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

    public void testTableCreated()
    {
        int numTables = SqliteUtils.countTables(db, FavStop.DB.TABLE_NAME);
        assertEquals(1, numTables);
    }

    public void testFavoriteIsAdded()
    {
        long newId = store.addStop(TEST_ROUTE, TEST_DIR_E, TEST_STOP);
        assertTrue(newId != -1);

        int totalRows = SqliteUtils.countRows(db, FavStop.DB.TABLE_NAME);
        assertEquals(1, totalRows);

        assertSingleTestStopInDb();
    }

    public void testDuplicateAddedWithoutErrors()
    {
        long newId = store.addStop(TEST_ROUTE, TEST_DIR_E, TEST_STOP);
        assertTrue(newId != -1);
        newId = store.addStop(TEST_ROUTE, TEST_DIR_E, TEST_STOP);
        assertTrue(newId != -1);
        newId = store.addStop(TEST_ROUTE, TEST_DIR_E, TEST_STOP);
        assertTrue(newId != -1);

        long totalRows = SqliteUtils.countRows(db, FavStop.DB.TABLE_NAME);
        assertEquals(1, totalRows);

        assertSingleTestStopInDb();
    }

    public void testGetFavoritesSingleRouteDir()
    {
        Stop stopA = new Stop(TEST_ID + 1, TEST_NAME + "_A");
        Stop stopB = new Stop(TEST_ID + 2, TEST_NAME + "_B");
        Stop stopC = new Stop(TEST_ID + 3, TEST_NAME + "_C");
        Stop stopD = new Stop(TEST_ID + 4, TEST_NAME + "_D");
        List<Stop> allStops = Lists.newArrayList(stopA, stopB, stopC, stopD);

        store.addStop(TEST_ROUTE, TEST_DIR_E, stopA);
        store.addStop(TEST_ROUTE, TEST_DIR_E, stopB);
        store.addStop(TEST_ROUTE, TEST_DIR_E, stopC);
        store.addStop(TEST_ROUTE, TEST_DIR_E, stopD);

        List<Stop> actualStops = store.getStops(TEST_ROUTE, TEST_DIR_E);
        assertEquals(allStops, actualStops);
    }

    public void testGetFavoritesMultipleRoutesDir()
    {
        Stop stopA = new Stop(TEST_ID + 1, TEST_NAME + "_A");
        Stop stopB = new Stop(TEST_ID + 2, TEST_NAME + "_B");
        Stop stopC = new Stop(TEST_ID + 3, TEST_NAME + "_C");
        Stop stopD = new Stop(TEST_ID + 4, TEST_NAME + "_D");
        Stop stopE = new Stop(TEST_ID + 5, TEST_NAME + "_E");
        List<Stop> stops1 = Lists.newArrayList(stopA, stopD, stopE);
        List<Stop> stops2 = Lists.newArrayList(stopB, stopC);

        store.addStop(TEST_ROUTE, TEST_DIR_W, stopA);
        store.addStop(TEST_ROUTE_2, TEST_DIR_E, stopB);
        store.addStop(TEST_ROUTE_2, TEST_DIR_E, stopC);
        store.addStop(TEST_ROUTE, TEST_DIR_W, stopD);
        store.addStop(TEST_ROUTE, TEST_DIR_W, stopE);

        List<Stop> actualStops1 = store.getStops(TEST_ROUTE, TEST_DIR_W);
        assertEquals(stops1, actualStops1);

        List<Stop> actualStops2 = store.getStops(TEST_ROUTE_2, TEST_DIR_E);
        assertEquals(stops2, actualStops2);
    }

    private void assertSingleTestStopInDb()
    {
        String[] cols = {FavStop.DB.ID,
                FavStop.DB.NAME,
                FavStop.DB.ROUTE_ID,
                FavStop.DB.ROUTE_DIR
                };
        String selection = FavStop.DB.ID + " = ?";
        String[] selectionArgs = {String.valueOf(TEST_STOP.getId())};
        Cursor cursor = db.query(FavStop.DB.TABLE_NAME, cols,
                selection, selectionArgs, null, null, null);

        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());

        cursor.moveToFirst();

        String actualIdStr = cursor.getString(cursor.getColumnIndexOrThrow(FavStop.DB.ID));
        int actualId = Integer.valueOf(actualIdStr);
        String actualName = cursor.getString(cursor.getColumnIndexOrThrow(FavStop.DB.NAME));
        Stop actualStop = new Stop(actualId, actualName);
        assertEquals(TEST_STOP, actualStop);

        String actualRouteId = cursor.getString(
                cursor.getColumnIndexOrThrow(FavStop.DB.ROUTE_ID));
        assertEquals(TEST_ROUTE.getId(), actualRouteId);

        String actualRouteDir = cursor.getString(
                cursor.getColumnIndexOrThrow(FavStop.DB.ROUTE_DIR));
        assertEquals(TEST_DIR_E.toString(), actualRouteDir);
    }
}
