package com.weizilla.transit.favorites.sqlite;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.favorites.BusFavoritesStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteDataSource;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.TreeSet;

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
            createTable(connection);
            return store;
        }
    }

    @Override
    public void saveFavorite(Route route)
    {
        String id = route.getId();
        try
        (
            Connection connection = createConnection();
            Statement statement = connection.createStatement()
        )
        {
            int numUpdated = statement.executeUpdate("INSERT INTO fav_routes (id) VALUES ('" + id + "')");
            if (numUpdated == 0)
            {
                logger.warn("No rows updated when saving favorite route id {}", id);
            }
        }
        catch (SQLException e)
        {
            logger.error("Sql error saving favorite route id {}: {}", id, e.getMessage(), e);
        }
    }

    @Override
    public Collection<String> getFavoriteRoutes()
    {
        Collection<String> routeIds = new TreeSet<>();

        try
        (
            Connection connection = createConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM fav_routes")
        )
        {
            while(resultSet.next())
            {
                routeIds.add(resultSet.getString("id"));
            }
        }
        catch (SQLException e)
        {
            logger.error("Sql error getting favorite routes: {}", e.getMessage(), e);
        }

        return routeIds;
    }

    private Connection createConnection() throws SQLException
    {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + dbPath);
        return dataSource.getConnection();
    }

    protected static void createTable(Connection connection)
    {
        String sqlFile = "create_routes_table.sql";
        try
        (
            Statement statement = connection.createStatement()
        )
        {
            URL url = Resources.getResource(sqlFile);
            String sql = Resources.toString(url, Charsets.UTF_8);
            statement.executeUpdate(sql);
        }
        catch (SQLException e)
        {
            logger.error("Error creating favorite routes table: {}", e.getMessage(), e);
        }
        catch (IOException e)
        {
            logger.error("Error reading create table sql file {}: {}", sqlFile, e.getMessage(), e);
        }
    }
}
