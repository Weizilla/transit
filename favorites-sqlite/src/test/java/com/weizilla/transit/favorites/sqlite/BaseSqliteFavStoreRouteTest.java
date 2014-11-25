package com.weizilla.transit.favorites.sqlite;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.favorites.BusFavoritesStore;
import com.weizilla.transit.sqlite.BaseSqliteTest;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.weizilla.transit.favorites.sqlite.Favorites.RouteEntry.TABLE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class BaseSqliteFavStoreRouteTest extends BaseSqliteTest
{
    protected BusFavoritesStore store;

    @Test
    public void storeIsSet() throws Exception
    {
        assertNotNull(store);
    }

    @Test
    public void getRouteIdsReturnsDbData() throws Exception
    {
        Set<String> expected = Sets.newHashSet("22", "36", "54A");
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(),
            readDataSet("get_routes.xml"));

        Collection<String> actualIds = store.getFavoriteRouteIds();
        assertEquals(expected, new HashSet<>(actualIds));
    }

    @Test
    public void savesFavoriteRoutes() throws Exception
    {
        Collection<String> routeIds = Lists.newArrayList("134", "156", "J14");
        IDataSet expected = readDataSet("save_routes.xml");
        ITable expectedTable = expected.getTable(TABLE_NAME);

        DatabaseOperation.DELETE_ALL.execute(databaseTester.getConnection(), expected);

        for (String routeId : routeIds)
        {
            store.saveFavorite(new Route(routeId));
        }

        ITable actual = getTable(TABLE_NAME);
        ITable actualFiltered = DefaultColumnFilter.includedColumnsTable(actual,
            expectedTable.getTableMetaData().getColumns());

        Assertion.assertEquals(expectedTable, actualFiltered);
    }

}