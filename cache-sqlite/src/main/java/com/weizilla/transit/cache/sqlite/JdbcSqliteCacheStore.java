package com.weizilla.transit.cache.sqlite;

import com.google.common.base.Joiner;
import com.weizilla.transit.cache.CacheStore;
import com.weizilla.transit.cache.sqlite.Cache.RouteEntry;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.sqlite.SqliteStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.weizilla.transit.utils.ResourceUtils.readFile;

public class JdbcSqliteCacheStore extends SqliteStore implements CacheStore
{
    private static final Logger logger = LoggerFactory.getLogger(JdbcSqliteCacheStore.class);

    private JdbcSqliteCacheStore(Path dbPath)
    {
        super(dbPath);
    }

    public static JdbcSqliteCacheStore createStore(Path dbPath) throws SQLException
    {
        JdbcSqliteCacheStore store = new JdbcSqliteCacheStore(dbPath);
        try
        (
            Connection connection = store.createConnection()
        )
        {
            executeSqlFromFile(connection, "cache/create_routes_table.sql" );
            executeSqlFromFile(connection, "cache/create_stops_table.sql" );
        }
        return store;
    }

    @Override
    public Map<Integer, Stop> getStops(Collection<Integer> stopIds)
    {
        if (stopIds.isEmpty()) {
            return Collections.emptyMap();
        }

        String sqlFile= "cache/get_stops.sql";
        Map<Integer, Stop> stops = new HashMap<>(stopIds.size());
        try
        (
            Connection connection = createConnection();
            PreparedStatement statement = createGetStatement(connection, stopIds, sqlFile);
            ResultSet resultSet = statement.executeQuery()
        )
        {
            while (resultSet.next())
            {
                int stopId = resultSet.getInt(Cache.StopEntry.ID);
                String stopName = resultSet.getString(Cache.StopEntry.NAME);
                stops.put(stopId, new Stop(stopId, stopName));
            }
        }
        catch (IOException e)
        {
            logger.error("Error reading sql file {}", sqlFile, e);
        }
        catch (SQLException e)
        {
            logger.error("Sql error getting stops", e);
        }
        return stops;
    }

    @Override
    public Map<String, Route> getRoutes(Collection<String> routeIds)
    {
        if (routeIds.isEmpty())
        {
            return Collections.emptyMap();
        }
        String sqlFile = "cache/get_routes.sql";
        Map<String, Route> routes = new HashMap<>(routeIds.size());
        try
        (
            Connection connection = createConnection();
            PreparedStatement statement = createGetStatement(connection, routeIds, sqlFile);
            ResultSet resultSet = statement.executeQuery()
        )
        {
            while (resultSet.next())
            {
                String routeId = resultSet.getString(RouteEntry.ID);
                String routeName = resultSet.getString(RouteEntry.NAME);
                routes.put(routeId, new Route(routeId, routeName));
            }
        }
        catch (IOException e)
        {
            logger.error("Error reading sql file {}", sqlFile, e);
        }
        catch (SQLException e)
        {
            logger.error("Sql error getting routes", e);
        }
        return routes;
    }


    private static <T> PreparedStatement createGetStatement(Connection connection,
        Collection<T> ids, String sqlFile) throws IOException, SQLException
    {
        String sql = readFile(sqlFile);
        sql += " (" + Joiner.on("," ).join(Collections.nCopies(ids.size(), "?" )) + ')';
        PreparedStatement statement = connection.prepareStatement(sql);
        int i = 1;
        for (T id : ids)
        {
            statement.setObject(i++, id);
        }
        return statement;
    }

    @Override
    public void updateRoutes(Collection<Route> routes)
    {
        if (routes.isEmpty())
        {
            return;
        }
        String sqlFile = "cache/update_routes.sql";
        try
        (
            Connection connection = createConnection();
            PreparedStatement statement = connection.prepareStatement(readFile(sqlFile))
        )
        {
            executeSqlFromFile(connection, "cache/delete_all_routes.sql" );

            int numUpdated = updateRoutes(routes, statement);
            if (numUpdated == 0)
            {
                logger.warn("No rows updated when updating routes");
            }
            else if (numUpdated != routes.size())
            {
                logger.warn("Different num of rows updated vs input when updating routes");
            }
        }
        catch (IOException e)
        {
            logger.error("Error reading sql file {}", sqlFile, e);
        }
        catch (SQLException e)
        {
            logger.error("Sql error updating routes", e);
        }
    }

    private static int updateRoutes(Collection<Route> routes, PreparedStatement statement)
        throws SQLException
    {
        int numBatched = 0;
        int numUpdated = 0;
        for (Route route : routes)
        {
            statement.setString(1, route.getId());
            statement.setString(2, route.getName().trim());
            statement.addBatch();
            numBatched++;
            if (numBatched % 100 == 0)
            {
                numUpdated += numUpdated(statement.executeBatch());
            }
        }
        numUpdated += numUpdated(statement.executeBatch());
        return numUpdated;
    }

    @Override
    public void updateStops(Collection<Stop> stops)
    {
        if (stops.isEmpty())
        {
            return;
        }
        String sqlFile = "cache/update_stops.sql";
        try
        (
            Connection connection = createConnection();
            PreparedStatement statement = connection.prepareStatement(readFile(sqlFile))
        )
        {
            int numUpdated = updateStops(stops, statement);
            if (numUpdated == 0)
            {
                logger.warn("No rows updated when updating stops" );
            }
            else if (numUpdated != stops.size())
            {
                logger.warn("Different num of rows updated vs input when updating stops" );
            }
        }
        catch (IOException e)
        {
            logger.error("Error reading sql file {}", sqlFile, e);
        }
        catch (SQLException e)
        {
            logger.error("Sql error updating stops", e);
        }
    }

    private static int updateStops(Collection<Stop> stops, PreparedStatement statement)
        throws SQLException
    {
        int numBatched = 0;
        int numUpdated = 0;
        for (Stop stop : stops)
        {
            statement.setInt(1, stop.getId());
            statement.setString(2, stop.getName().trim());
            statement.addBatch();
            numBatched++;
            if (numBatched % 100 == 0)
            {
                numUpdated += numUpdated(statement.executeBatch());
            }
        }
        numUpdated += numUpdated(statement.executeBatch());
        return numUpdated;
    }

    private static int numUpdated(int[] status)
    {
        int num = 0;
        for (int i : status)
        {
            if (i > 0)
            {
                num += i;
            }
        }
        return num;
    }
}
