package com.weizilla.transit.groups.sqlite;

import com.weizilla.transit.groups.BusGroupsStore;
import com.weizilla.transit.groups.Group;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.weizilla.transit.utils.ResourceUtils.readFile;

//TODO combine this with SqliteFavoritesStore
public class JdbcSqliteGroupsStore implements BusGroupsStore
{
    private static final Logger logger = LoggerFactory.getLogger(JdbcSqliteGroupsStore.class);
    private final Path dbPath;

    private JdbcSqliteGroupsStore(Path dbPath)
    {
        this.dbPath = dbPath;
    }

    public static JdbcSqliteGroupsStore createStore(Path dbPath) throws SQLException
    {
        JdbcSqliteGroupsStore store = new JdbcSqliteGroupsStore(dbPath);
        try
        (
            Connection connection = store.createConnection()
        )
        {
            createGroupsTable(connection);
            return store;
        }
    }

    @Override
    public Group createGroup(String name)
    {
        String sqlFile = "create_group.sql";
        try
        (
            Connection conn = createConnection();
            PreparedStatement statement = conn.prepareStatement(readFile(sqlFile))
        )
        {
            statement.setString(1, name);
            int numUpdated = statement.executeUpdate();
            if (numUpdated == 0)
            {
                logger.warn("No rows updated when creating group with name {}", name);
            }
            try
            (
                ResultSet key = statement.getGeneratedKeys()
            )
            {
                int id = key.getInt(1);
                return new Group(id, name);
            }
        }
        catch (IOException e)
        {
            logger.error("Error reading sql file {}", sqlFile, e);
        }
        catch (SQLException e)
        {
            logger.error("Sql error creating group name {}", name, e);
            if (e.getMessage().contains("SQLITE_CONSTRAINT"))
            {
                throw new DuplicateGroupException(name);
            }
        }
        //TODO don't use status return codes
        return null;
    }

    @Override
    public Set<Group> getAllGroups()
    {
        Set<Group> groups = new HashSet<>();
        String sqlFile = "get_all_groups.sql";
        try
        (
            Connection conn = createConnection();
            PreparedStatement statement = conn.prepareStatement(readFile(sqlFile));
            ResultSet resultSet = statement.executeQuery();
        )
        {
            while (resultSet.next())
            {
                int id = resultSet.getInt("_id");
                String name = resultSet.getString("name");
                groups.add(new Group(id, name));
            }
            return groups;
        }
        catch (IOException e)
        {
            logger.error("Error reading sql file {}", sqlFile, e);
        }
        catch (SQLException e)
        {
            logger.error("Sql error getting all groups: {}", e.getMessage(), e);
        }

        return Collections.emptySet();
    }

    @Override
    public void deleteGroup(int id)
    {
        String sqlFile = "delete_group.sql";
        try
        (
            Connection connection = createConnection();
            PreparedStatement statement = createDeleteGroupStatement(connection, sqlFile, id)
        )
        {
            int numDeleted = statement.executeUpdate();
            if (numDeleted == 0)
            {
                logger.warn("No rows deleted when deleting group id {}", id);
            }
        }
        catch (IOException e)
        {
            logger.error("Error reading sql file {}", sqlFile, e);
        }
        catch (SQLException e)
        {
            logger.error("Sql error getting all groups: {}", e.getMessage(), e);
        }
    }

    private static PreparedStatement createDeleteGroupStatement(Connection connection, String sqlFile, int groupId)
        throws SQLException, IOException
    {
        String sql = readFile(sqlFile);
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, groupId);
        return statement;
    }

    private Connection createConnection() throws SQLException
    {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + dbPath);
        return dataSource.getConnection();
    }

    protected static void createGroupsTable(Connection connection)
    {
        createTable(connection, "create_groups_table.sql");
    }

    private static void createTable(Connection connection, String sqlFile)
    {
        try
        (
            Statement statement = connection.createStatement()
        )
        {
            String sql = readFile(sqlFile);
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
