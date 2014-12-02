package com.weizilla.transit.android.db;

import com.google.common.collect.Lists;
import com.weizilla.transit.cache.sqlite.Cache;
import com.weizilla.transit.favorites.sqlite.Favorites;
import com.weizilla.transit.groups.sqlite.Groups;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.annotation.Config;

import java.util.List;

import static com.weizilla.transit.android.db.AndroidDbUtils.tableExists;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(manifest = Config.NONE)
public class DatabaseHelperTest extends AndroidSqliteTest
{
    private static final List<String> CREATION_FILES = Lists.newArrayList(
        "favorites/create_routes_table.sql",  "favorites/create_stops_table.sql",
        "groups/create_groups_table.sql", "groups/create_groups_stops_table.sql",
        "cache/create_routes_table.sql", "cache/create_stops_table.sql");
    private DatabaseHelper helper;

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        helper = new DatabaseHelper(null, CREATION_FILES);
    }

    //TODO test constructor

    @Test
    public void createsAllTablesOnCreate() throws Exception
    {
        assertFalse(tableExists(database, Favorites.RouteEntry.TABLE_NAME));
        assertFalse(tableExists(database, Favorites.StopEntry.TABLE_NAME));
        assertFalse(tableExists(database, Groups.GroupEntry.TABLE_NAME));
        assertFalse(tableExists(database, Groups.StopEntry.TABLE_NAME));
        assertFalse(tableExists(database, Cache.StopEntry.TABLE_NAME));
        assertFalse(tableExists(database, Cache.RouteEntry.TABLE_NAME));
        helper.onCreate(database);
        assertTrue(tableExists(database, Favorites.RouteEntry.TABLE_NAME));
        assertTrue(tableExists(database, Favorites.StopEntry.TABLE_NAME));
        assertTrue(tableExists(database, Groups.GroupEntry.TABLE_NAME));
        assertTrue(tableExists(database, Groups.StopEntry.TABLE_NAME));
        assertTrue(tableExists(database, Cache.RouteEntry.TABLE_NAME));
        assertTrue(tableExists(database, Cache.StopEntry.TABLE_NAME));
    }
}