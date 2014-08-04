package com.weizilla.transit.favorites.sqlite;

import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.favorites.BusFavoritesStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteDataSource;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.TreeSet;

public class SqliteFavoriteStore implements BusFavoritesStore
{
    private static final Logger logger = LoggerFactory.getLogger(SqliteFavoriteStore.class);
    private final Path dbPath;

    public SqliteFavoriteStore(Path dbPath)
    {
        this.dbPath = dbPath;
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
            int numUpdated = statement.executeUpdate("INSERT INTO fav_routes (id) VALUES (" + id + ")");
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
}
