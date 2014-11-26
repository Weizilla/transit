package com.weizilla.transit.favorites.sqlite;

import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.favorites.FavoritesStore;
import com.weizilla.transit.favorites.sqlite.Favorites.RouteEntry;
import com.weizilla.transit.favorites.sqlite.Favorites.StopEntry;
import com.weizilla.transit.sqlite.SqliteStore;
import com.weizilla.transit.utils.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.TreeSet;

public class JdbcSqliteFavoritesStore extends SqliteStore implements FavoritesStore
{
    private static final Logger logger = LoggerFactory.getLogger(JdbcSqliteFavoritesStore.class);

    private JdbcSqliteFavoritesStore(Path dbPath)
    {
        super(dbPath);
    }

    public static JdbcSqliteFavoritesStore createStore(Path dbPath) throws SQLException
    {
        JdbcSqliteFavoritesStore store = new JdbcSqliteFavoritesStore(dbPath);
        try
        (
            Connection connection = store.createConnection()
        )
        {
            executeSqlFromFile(connection, "create_routes_table.sql");
            executeSqlFromFile(connection, "create_stops_table.sql");
        }
        return store;
    }

    @Override
    public void saveFavorite(Route route)
    {
        String id = route.getId();
        String sqlFile = "save_route.sql";
        try
        (
            Connection connection = createConnection();
            PreparedStatement statement = connection.prepareStatement(ResourceUtils.readFile(sqlFile))
        )
        {
            statement.setString(1, id);
            int numUpdated = statement.executeUpdate();
            if (numUpdated == 0)
            {
                logger.warn("No rows updated when saving favorite route id {}", id);
            }
        }
        catch (IOException e)
        {
            logger.error("Error reading sql file {}", sqlFile, e);
        }
        catch (SQLException e)
        {
            logger.error("Sql error saving favorite route id {}: {}", id, e.getMessage(), e);
        }
    }


    @Override
    public void saveFavorite(Stop stop)
    {
        int id = stop.getId();
        String sqlFile = "save_stop.sql";
        try
        (
            Connection connection = createConnection();
            PreparedStatement statement = createSaveStopStmt(connection, stop, sqlFile)
        )
        {
            int numUpdated = statement.executeUpdate();
            if (numUpdated == 0)
            {
                logger.warn("No rows updated when saving favorite stop id {}", id);
            }
        }
        catch (IOException e)
        {
            logger.error("Error reading sql file {}", sqlFile, e);
        }
        catch (SQLException e)
        {
            logger.error("Sql error saving favorite stop id {}: {}", id, e.getMessage(), e);
        }
    }

    private static PreparedStatement createSaveStopStmt(Connection connection, Stop stop, String sqlFile)
        throws SQLException, IOException
    {
        String sql = ResourceUtils.readFile(sqlFile);
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, stop.getId());
        statement.setString(2, stop.getRouteId());
        statement.setObject(3, stop.getDirection());
        return statement;
    }

    @Override
    public Collection<String> getFavoriteRouteIds()
    {
        Collection<String> routeIds = new TreeSet<>();
        String sqlFile = "get_routes.sql";
        try
        (
            Connection connection = createConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(ResourceUtils.readFile(sqlFile))
        )
        {
            while (resultSet.next())
            {
                routeIds.add(resultSet.getString(RouteEntry.ID));
            }
        }
        catch (IOException e)
        {
            logger.error("Error reading sql file {}", sqlFile, e);
        }
        catch (SQLException e)
        {
            logger.error("Sql error getting favorite routes: {}", e.getMessage(), e);
        }
        return routeIds;
    }

    @Override
    public Collection<Integer> getFavoriteStopIds(String route, Direction direction)
    {
        Collection<Integer> stopIds = new TreeSet<>();
        String sqlFile = "get_stops.sql";
        try
        (
            Connection conn = createConnection();
            PreparedStatement statement = createGetStopsStatement(conn, sqlFile, route, direction);
            ResultSet resultSet = statement.executeQuery()
        )
        {
            while (resultSet.next())
            {
                stopIds.add(resultSet.getInt(StopEntry.ID));
            }
        }
        catch (IOException e)
        {
            logger.error("Error reading sql file {}", sqlFile, e);
        }
        catch (SQLException e)
        {
            logger.error("Sql error getting favorite stops: {}", e.getMessage(), e);
        }
        return stopIds;
    }

    private static PreparedStatement createGetStopsStatement(
        Connection connection, String sqlFile, String route, Direction direction)
        throws IOException, SQLException
    {
        String sql = ResourceUtils.readFile(sqlFile);
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, route);
        statement.setObject(2, direction);
        return statement;
    }

}
