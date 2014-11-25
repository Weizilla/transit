package com.weizilla.transit.sqlite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteDataSource;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.weizilla.transit.utils.ResourceUtils.readFile;

public abstract class SqliteStore
{
    private static final Logger logger = LoggerFactory.getLogger(SqliteStore.class);
    protected Path dbPath;

    protected SqliteStore(Path dbPath)
    {
        this.dbPath = dbPath;
    }

    protected Connection createConnection() throws SQLException
    {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + dbPath);
        Connection connection = dataSource.getConnection();
        enableForeignKeyConstraints(connection);
        return connection;
    }

    private void enableForeignKeyConstraints(Connection conn) throws SQLException
    {
        try
        (
            Statement statement = conn.createStatement()
        )
        {
            statement.execute("PRAGMA foreign_keys = ON");
        }
    }

    protected static void executeSqlFromFile(Connection connection, String sqlFile)
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
            logger.error("Error executing sql file {}: {}", sqlFile, e.getMessage(), e);
        }
        catch (IOException e)
        {
            logger.error("Error reading sql file {}: {}", sqlFile, e.getMessage(), e);
        }
    }

}
