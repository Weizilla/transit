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
    private static final Stop TEST_STOP = new Stop(TEST_ID, TEST_NAME, true);
    private static final Stop TEST_STOP_1 = new Stop(TEST_ID + 1, TEST_NAME + "_1", true);
    private static final Stop TEST_STOP_2 = new Stop(TEST_ID + 2, TEST_NAME + "_2", true);
    private static final Stop TEST_STOP_3 = new Stop(TEST_ID + 3, TEST_NAME + "_3", true);
    private static final Stop TEST_STOP_4 = new Stop(TEST_ID + 4, TEST_NAME + "_4", true);
    private static final Stop TEST_STOP_5 = new Stop(TEST_ID + 5, TEST_NAME + "_5", true);
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

    public void testFavoriteIsRemoved()
    {
        long newId = store.addStop(TEST_ROUTE, TEST_DIR_E, TEST_STOP);
        assertTrue(newId != -1);

        assertSingleTestStopInDb();

        boolean deleted = store.removeStop(TEST_STOP);
        assertTrue(deleted);

        int totalRows = SqliteUtils.countRows(db, FavStop.DB.TABLE_NAME);
        assertEquals(0, totalRows);
    }

    public void testSecondDuplicateDeleteDoesNothing()
    {
        long newId = store.addStop(TEST_ROUTE, TEST_DIR_E, TEST_STOP);
        assertTrue(newId != -1);

        assertSingleTestStopInDb();

        boolean deleted = store.removeStop(TEST_STOP);
        assertTrue(deleted);

        int totalRows = SqliteUtils.countRows(db, FavStop.DB.TABLE_NAME);
        assertEquals(0, totalRows);

        deleted = store.removeStop(TEST_STOP);
        assertFalse(deleted);
    }

    public void testOnlyProperFavIsRemoved()
    {
        long newId = store.addStop(TEST_ROUTE, TEST_DIR_E, TEST_STOP_1);
        assertTrue(newId != -1);
        newId = store.addStop(TEST_ROUTE_2, TEST_DIR_W, TEST_STOP_2);
        assertTrue(newId != -1);
        newId = store.addStop(TEST_ROUTE, TEST_DIR_E, TEST_STOP_3);
        assertTrue(newId != -1);
        newId = store.addStop(TEST_ROUTE, TEST_DIR_W, TEST_STOP_4);
        assertTrue(newId != -1);
        newId = store.addStop(TEST_ROUTE_2, TEST_DIR_W, TEST_STOP_5);
        assertTrue(newId != -1);

        int totalRows = SqliteUtils.countRows(db, FavStop.DB.TABLE_NAME);
        assertEquals(5, totalRows);

        boolean deleted = store.removeStop(TEST_STOP_3);
        assertTrue(deleted);

        totalRows = SqliteUtils.countRows(db, FavStop.DB.TABLE_NAME);
        assertEquals(4, totalRows);

        assertSingleStopInDb(TEST_ROUTE, TEST_DIR_E, TEST_STOP_1);
        assertSingleStopInDb(TEST_ROUTE_2, TEST_DIR_W, TEST_STOP_2);
        assertSingleStopInDb(TEST_ROUTE, TEST_DIR_W, TEST_STOP_4);
        assertSingleStopInDb(TEST_ROUTE_2, TEST_DIR_W, TEST_STOP_5);
    }

    public void testGetFavoritesSingleRouteDir()
    {
        List<Stop> allStops = Lists.newArrayList(
                TEST_STOP_1,
                TEST_STOP_2,
                TEST_STOP_3,
                TEST_STOP_4
        );

        store.addStop(TEST_ROUTE, TEST_DIR_E, TEST_STOP_1);
        store.addStop(TEST_ROUTE, TEST_DIR_E, TEST_STOP_2);
        store.addStop(TEST_ROUTE, TEST_DIR_E, TEST_STOP_3);
        store.addStop(TEST_ROUTE, TEST_DIR_E, TEST_STOP_4);

        List<Stop> actualStops = store.getStops(TEST_ROUTE, TEST_DIR_E);
        assertEquals(allStops, actualStops);
    }

    public void testGetFavoritesMultipleRoutesDir()
    {
        List<Stop> stops1 = Lists.newArrayList(
                TEST_STOP_1,
                TEST_STOP_4,
                TEST_STOP_5);
        List<Stop> stops2 = Lists.newArrayList(
                TEST_STOP_2,
                TEST_STOP_3);

        store.addStop(TEST_ROUTE, TEST_DIR_W, TEST_STOP_1);
        store.addStop(TEST_ROUTE_2, TEST_DIR_E, TEST_STOP_2);
        store.addStop(TEST_ROUTE_2, TEST_DIR_E, TEST_STOP_3);
        store.addStop(TEST_ROUTE, TEST_DIR_W, TEST_STOP_4);
        store.addStop(TEST_ROUTE, TEST_DIR_W, TEST_STOP_5);

        List<Stop> actualStops1 = store.getStops(TEST_ROUTE, TEST_DIR_W);
        assertEquals(stops1, actualStops1);

        List<Stop> actualStops2 = store.getStops(TEST_ROUTE_2, TEST_DIR_E);
        assertEquals(stops2, actualStops2);
    }

    private void assertSingleTestStopInDb()
    {
        assertSingleStopInDb(TEST_ROUTE, TEST_DIR_E, TEST_STOP);
    }

    private void assertSingleStopInDb(Route route, Direction direction, Stop stop)
    {
        String[] cols = {FavStop.DB.ID,
                FavStop.DB.NAME,
                FavStop.DB.ROUTE_ID,
                FavStop.DB.ROUTE_DIR
        };
        String selection = FavStop.DB.ID + " = ?";
        String[] selectionArgs = {String.valueOf(stop.getId())};
        Cursor cursor = db.query(FavStop.DB.TABLE_NAME, cols,
                selection, selectionArgs, null, null, null);

        try
        {
            assertNotNull(cursor);
            assertEquals(1, cursor.getCount());

            cursor.moveToFirst();

            String actualIdStr = cursor.getString(cursor.getColumnIndexOrThrow(FavStop.DB.ID));
            int actualId = Integer.valueOf(actualIdStr);
            String actualName = cursor.getString(cursor.getColumnIndexOrThrow(FavStop.DB.NAME));
            Stop actualStop = new Stop(actualId, actualName, true);
            assertEquals(stop, actualStop);

            String actualRouteId = cursor.getString(
                    cursor.getColumnIndexOrThrow(FavStop.DB.ROUTE_ID));
            assertEquals(route.getId(), actualRouteId);

            String actualRouteDir = cursor.getString(
                    cursor.getColumnIndexOrThrow(FavStop.DB.ROUTE_DIR));
            assertEquals(direction.toString(), actualRouteDir);
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
