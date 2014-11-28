package com.weizilla.transit.cache.sqlite;

import com.weizilla.transit.cache.BaseCacheStoreTest;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class JdbcSqliteCacheStoreTest extends BaseCacheStoreTest
{
    private static final Logger logger = LoggerFactory.getLogger(JdbcSqliteCacheStoreTest.class);

    @Before
    public void setUp() throws Exception
    {
        Path dbPath = Files.createTempFile(getClass().getSimpleName() + '-', ".db");
        File dbFile = dbPath.toFile();
        dbFile.deleteOnExit();
        logger.debug("Temp db file: {}", dbPath);
        store = JdbcSqliteCacheStore.createStore(dbPath);
    }
}
