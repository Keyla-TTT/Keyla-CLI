package org.keyla.api

import org.keyla.models.*
import kotlin.test.*

class ConfigServiceTest {
    
    @Test
    fun `ConfigListResponse should have correct structure`() {
        val response = ConfigListResponse(
            entries = listOf(
                ConfigEntry(
                    key = ConfigKey("database", "mongoUri"),
                    value = "mongodb://localhost:27017",
                    description = "MongoDB connection URI",
                    dataType = "string",
                    defaultValue = "mongodb://localhost:27017"
                ),
                ConfigEntry(
                    key = ConfigKey("server", "port"),
                    value = "8080",
                    description = "Server port",
                    dataType = "int",
                    defaultValue = "8080"
                )
            )
        )
        
        assertEquals(2, response.entries.size)
        assertEquals("database", response.entries[0].key.section)
        assertEquals("mongoUri", response.entries[0].key.key)
        assertEquals("server", response.entries[1].key.section)
        assertEquals("port", response.entries[1].key.key)
    }
    
    @Test
    fun `AppConfig should have correct structure`() {
        val config = AppConfig(
            database = DatabaseConfig(
                mongoUri = "mongodb://localhost:27017",
                databaseName = "keyla",
                useMongoDb = true
            ),
            server = ServerConfig(
                host = "localhost",
                port = 8080,
                threadPool = ThreadPoolConfig(
                    coreSize = 4,
                    maxSize = 8,
                    keepAliveSeconds = 60,
                    queueSize = 100,
                    threadNamePrefix = "keyla-"
                )
            ),
            dictionary = DictionaryConfig(
                basePath = "/usr/share/dict",
                autoCreateDirectories = true
            ),
            version = "1.0.0"
        )
        
        assertEquals("mongodb://localhost:27017", config.database.mongoUri)
        assertEquals("keyla", config.database.databaseName)
        assertTrue(config.database.useMongoDb)
        assertEquals("localhost", config.server.host)
        assertEquals(8080, config.server.port)
        assertEquals(4, config.server.threadPool.coreSize)
        assertEquals(8, config.server.threadPool.maxSize)
        assertEquals("/usr/share/dict", config.dictionary.basePath)
        assertTrue(config.dictionary.autoCreateDirectories)
        assertEquals("1.0.0", config.version)
    }
    
    @Test
    fun `ConfigEntry should have correct structure`() {
        val entry = ConfigEntry(
            key = ConfigKey("database", "mongoUri"),
            value = "mongodb://localhost:27017",
            description = "MongoDB connection URI",
            dataType = "string",
            defaultValue = "mongodb://localhost:27017"
        )
        
        assertEquals("database", entry.key.section)
        assertEquals("mongoUri", entry.key.key)
        assertEquals("mongodb://localhost:27017", entry.value)
        assertEquals("MongoDB connection URI", entry.description)
        assertEquals("string", entry.dataType)
        assertEquals("mongodb://localhost:27017", entry.defaultValue)
    }
    
    @Test
    fun `ConfigUpdateResponse should have correct structure`() {
        val response = ConfigUpdateResponse(
            success = true,
            message = "Configuration updated successfully"
        )
        
        assertTrue(response.success)
        assertEquals("Configuration updated successfully", response.message)
    }
    
    @Test
    fun `SimpleConfigUpdateRequest should have correct structure`() {
        val request = SimpleConfigUpdateRequest(
            key = "database.mongoUri",
            value = "mongodb://new-host:27017"
        )
        
        assertEquals("database.mongoUri", request.key)
        assertEquals("mongodb://new-host:27017", request.value)
    }
    
    @Test
    fun `ConfigKey should have correct structure`() {
        val key = ConfigKey("database", "mongoUri")
        
        assertEquals("database", key.section)
        assertEquals("mongoUri", key.key)
    }
} 