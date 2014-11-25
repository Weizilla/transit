package com.weizilla.transit.groups.sqlite;

import com.weizilla.transit.bus.data.Stop;
import com.weizilla.transit.groups.BusGroupsStore;
import com.weizilla.transit.groups.Group;
import com.weizilla.transit.groups.sqlite.Groups.GroupEntry;
import com.weizilla.transit.sqlite.SqliteStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteErrorCode;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.weizilla.transit.utils.ResourceUtils.readFile;

public class JdbcSqliteGroupsStore extends SqliteStore implements BusGroupsStore
{
    private static final Logger logger = LoggerFactory.getLogger(JdbcSqliteGroupsStore.class);

    private JdbcSqliteGroupsStore(Path dbPath)
    {
        super(dbPath);
    }

    public static JdbcSqliteGroupsStore createStore(Path dbPath) throws SQLException
    {
        JdbcSqliteGroupsStore store = new JdbcSqliteGroupsStore(dbPath);
        try
        (
            Connection connection = store.createConnection()
        )
        {
            executeSqlFromFile(connection, "create_groups_table.sql");
            executeSqlFromFile(connection, "create_stops_table.sql");
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
            if (isConstraintException(e))
            {
                throw new DuplicateGroupException(name, e);
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
                int id = resultSet.getInt(GroupEntry._ID);
                String name = resultSet.getString(GroupEntry.NAME);
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
            PreparedStatement statement = connection.prepareStatement(readFile(sqlFile))
        )
        {
            statement.setInt(1, id);
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
            logger.error("Sql error deleting group: {}", e.getMessage(), e);
        }
    }

    @Override
    public void addToGroup(int groupId, Stop stop)
    {
        String sqlFile = "add_stop.sql";
        try
        (
            Connection conn = createConnection();
            PreparedStatement stmt = conn.prepareStatement(readFile(sqlFile))
        )
        {
            stmt.setInt(1, groupId);
            stmt.setInt(2, stop.getId());
            stmt.setString(3, stop.getName());
            int numUpdated = stmt.executeUpdate();
            if (numUpdated == 0)
            {
                logger.warn("No rows updated when adding stop {} to group {}", stop, groupId);
            }
        }
        catch (IOException e)
        {
            logger.error("Error reading sql file {}", sqlFile, e);
        }
        catch (SQLException e)
        {
            if (isConstraintException(e)) {
                throw new InvalidGroupException(groupId, e);
            }
            logger.error("Sql error adding stop {} to group {}", stop, groupId, e);
        }
    }

    @Override
    public void removeFromGroup(int groupId, int stopId)
    {
        String sqlFile = "remove_stop.sql";
        try
        (
            Connection connection = createConnection();
            PreparedStatement statement = connection.prepareStatement(readFile(sqlFile));
        )
        {
            statement.setInt(1, groupId);
            statement.setInt(2, stopId);
            int numDeleted = statement.executeUpdate();
            if (numDeleted == 0)
            {
                logger.warn("No rows deleted when delete stop {} from group {}", stopId, groupId);
            }
        }
        catch (IOException e)
        {
            logger.error("Error reading sql file {}", sqlFile, e);
        }
        catch (SQLException e)
        {
            logger.error("Sql error removing stop {} from group {}: {}", stopId, groupId,
                e.getMessage(), e);
        }

    }

    @Override
    public void renameGroup(int id, String newName)
    {

    }

    private static boolean isConstraintException(SQLException e)
    {
        return e.getMessage().contains(SQLiteErrorCode.SQLITE_CONSTRAINT.message);
    }
}
