package com.weizilla.transit.favorites.sqlite;

import com.google.common.collect.Sets;
import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Stop;
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

import static com.weizilla.transit.favorites.sqlite.Favorites.StopEntry.TABLE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class BaseSqliteFavStoreStopTest extends BaseSqliteTest
{
    protected BusFavoritesStore store;

    @Test
    public void storeIsSet() throws Exception
    {
        assertNotNull(store);
    }

    @Test
    public void getStopIdsReturnsDbData() throws Exception
    {
        String route = "22";
        Direction direction = Direction.Northbound;
        Set<Integer> expected = Sets.newHashSet(100, 400);
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(), readDataSet("get_stops.xml"));

        Collection<Integer> actualIds = store.getFavoriteStopIds(route, direction);
        assertEquals(expected, new HashSet<>(actualIds));
    }

    @Test
    public void savesFavoriteStops() throws Exception
    {
        IDataSet expected = readDataSet("save_stops.xml");
        ITable expectedTable = expected.getTable(TABLE_NAME);

        DatabaseOperation.DELETE_ALL.execute(databaseTester.getConnection(), expected);

        store.saveFavorite(new Stop(100, "36", Direction.Northbound));
        store.saveFavorite(new Stop(200, "N22", Direction.Westbound));
        store.saveFavorite(new Stop(300, "156", Direction.Eastbound));
        store.saveFavorite(new Stop(400, "N22", Direction.Westbound));

        ITable actual = getTable(TABLE_NAME);
        ITable actualFiltered = DefaultColumnFilter.includedColumnsTable(actual,
            expectedTable.getTableMetaData().getColumns());

        Assertion.assertEquals(expectedTable, actualFiltered);
    }
}