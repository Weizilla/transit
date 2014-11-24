package com.weizilla.transit.favorites.android.db;

import com.weizilla.transit.favorites.sqlite.Favorites.RoutesEntry;
import com.weizilla.transit.favorites.sqlite.Favorites.StopsEntry;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.annotation.Config;

import static com.weizilla.transit.favorites.android.db.AndroidDbUtils.tableExists;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(manifest = Config.NONE)
public class FavoritesDbHelperTest extends AndroidSqliteTest
{
    private FavoritesDbHelper helper;

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        helper = new FavoritesDbHelper(null);
    }

    //TODO test constructor

    @Test
    public void createsTablesOnCreate() throws Exception
    {
        assertFalse(tableExists(database, RoutesEntry.TABLE_NAME));
        assertFalse(tableExists(database, StopsEntry.TABLE_NAME));
        helper.onCreate(database);
        assertTrue(tableExists(database, RoutesEntry.TABLE_NAME));
        assertTrue(tableExists(database, StopsEntry.TABLE_NAME));
    }
}