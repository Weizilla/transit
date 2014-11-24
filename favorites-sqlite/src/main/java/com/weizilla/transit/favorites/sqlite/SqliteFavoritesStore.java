package com.weizilla.transit.favorites.sqlite;

import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;
import com.weizilla.transit.favorites.BusFavoritesStore;
import com.weizilla.transit.utils.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteDataSource;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.TreeSet;

@SuppressWarnings("Annotator")
public class SqliteFavoritesStore implements BusFavoritesStore
{
    private static final Logger logger = LoggerFactory.getLogger(SqliteFavoritesStore.class);
    private final Path dbPath;

    private SqliteFavoritesStore(Path dbPath)
    {
        this.dbPath = dbPath;
    }

    public static SqliteFavoritesStore createStore(Path dbPath) throws SQLException
    {
        SqliteFavoritesStore store = new SqliteFavoritesStore(dbPath);
        try
        (
            Connection connection = store.createConnection()
        )
        {
            createRoutesTable(connection);
            createStopsTable(connection);
            return store;
        }
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
            PreparedStatement statement = createSaveStopStatement(connection, stop, sqlFile)
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

    private static PreparedStatement createSaveStopStatement(Connection connection, Stop stop, String sqlFile)
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
                routeIds.add(resultSet.getString("id"));
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
            Connection connection = createConnection();
            PreparedStatement statement = createGetStopsStatement(connection, sqlFile, route, direction);
            ResultSet resultSet = statement.executeQuery()
        )
        {
            while (resultSet.next())
            {
                stopIds.add(resultSet.getInt("id"));
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

    private Connection createConnection() throws SQLException
    {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + dbPath);
        return dataSource.getConnection();
    }

    protected static void createRoutesTable(Connection connection)
    {
        createTable(connection, "create_routes_table.sql");
    }

    protected static void createStopsTable(Connection connection)
    {
        createTable(connection, "create_stops_table.sql");
    }

    private static void createTable(Connection connection, String sqlFile)
    {
        try
        (
            Statement statement = connection.createStatement()
        )
        {
            String sql = ResourceUtils.readFile(sqlFile);
            statement.executeUpdate(sql);
        }
        catch (SQLException e)
        {
            logger.error("Error creating table: {}", e.getMessage(), e);
        }
        catch (IOException e)
        {
            logger.error("Error reading sql file {}: {}", sqlFile, e.getMessage(), e);
        }
    }
}
