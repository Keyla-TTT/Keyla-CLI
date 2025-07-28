package org.keyla.models

import kotlin.test.*

class ApiModelsTest {
    
    @Test
    fun `TestRequest should have correct default values`() {
        val request = TestRequest(
            profileId = "profile123",
            wordCount = 50
        )
        
        assertEquals("profile123", request.profileId)
        assertEquals(50, request.wordCount)
        assertTrue(request.sources.isEmpty())
        assertTrue(request.modifiers.isEmpty())
        assertNull(request.timeLimit)
    }
    
    @Test
    fun `TestRequest should accept all parameters`() {
        val sources = listOf(SourceWithMerger("english", "standard"))
        val modifiers = listOf("punctuation", "numbers")
        val timeLimit = 300000L
        
        val request = TestRequest(
            profileId = "profile123",
            sources = sources,
            wordCount = 50,
            modifiers = modifiers,
            timeLimit = timeLimit
        )
        
        assertEquals("profile123", request.profileId)
        assertEquals(sources, request.sources)
        assertEquals(50, request.wordCount)
        assertEquals(modifiers, request.modifiers)
        assertEquals(timeLimit, request.timeLimit)
    }
    
    @Test
    fun `TestResponse should have correct structure`() {
        val response = TestResponse(
            testId = "test123",
            profileId = "profile123",
            words = listOf("hello", "world"),
            sources = listOf(SourceWithMerger("english", "standard")),
            modifiers = listOf("punctuation"),
            createdAt = "2024-01-01T00:00:00Z",
            completedAt = "2024-01-01T00:05:00Z",
            accuracy = 95.5,
            rawAccuracy = 97.0,
            testTime = 300000L,
            errorCount = 2,
            errorWordIndices = listOf(5, 12),
            timeLimit = 300000L
        )
        
        assertEquals("test123", response.testId)
        assertEquals("profile123", response.profileId)
        assertEquals(listOf("hello", "world"), response.words)
        assertEquals(95.5, response.accuracy)
        assertEquals(97.0, response.rawAccuracy)
        assertEquals(300000L, response.testTime)
        assertEquals(2, response.errorCount)
        assertEquals(listOf(5, 12), response.errorWordIndices)
        assertEquals(300000L, response.timeLimit)
    }
    
    @Test
    fun `TestResultsRequest should have correct structure`() {
        val results = TestResultsRequest(
            accuracy = 95.5,
            rawAccuracy = 97.0,
            testTime = 300000L,
            errorCount = 2,
            errorWordIndices = listOf(5, 12)
        )
        
        assertEquals(95.5, results.accuracy)
        assertEquals(97.0, results.rawAccuracy)
        assertEquals(300000L, results.testTime)
        assertEquals(2, results.errorCount)
        assertEquals(listOf(5, 12), results.errorWordIndices)
    }
    
    @Test
    fun `ProfileResponse should have correct structure`() {
        val profile = ProfileResponse(
            id = "profile123",
            name = "John Doe",
            email = "john@example.com",
            settings = setOf("dark_mode", "sound_enabled")
        )
        
        assertEquals("profile123", profile.id)
        assertEquals("John Doe", profile.name)
        assertEquals("john@example.com", profile.email)
        assertEquals(setOf("dark_mode", "sound_enabled"), profile.settings)
    }
    
    @Test
    fun `CreateProfileRequest should have correct structure`() {
        val request = CreateProfileRequest(
            name = "New User",
            email = "newuser@example.com",
            settings = setOf("dark_mode")
        )
        
        assertEquals("New User", request.name)
        assertEquals("newuser@example.com", request.email)
        assertEquals(setOf("dark_mode"), request.settings)
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
    fun `AnalyticsResponse should have correct structure`() {
        val analytics = AnalyticsResponse(
            userId = "user123",
            totalTests = 25,
            averageWpm = 65.5,
            averageAccuracy = 92.3,
            bestWpm = 85.0,
            worstWpm = 45.2,
            bestAccuracy = 98.5,
            worstAccuracy = 78.9,
            wpmImprovement = 12.5,
            accuracyImprovement = 5.2,
            totalErrors = 150,
            averageErrorsPerTest = 6.0
        )
        
        assertEquals("user123", analytics.userId)
        assertEquals(25, analytics.totalTests)
        assertEquals(65.5, analytics.averageWpm, 0.01)
        assertEquals(92.3, analytics.averageAccuracy, 0.01)
        assertEquals(85.0, analytics.bestWpm, 0.01)
        assertEquals(45.2, analytics.worstWpm, 0.01)
        assertEquals(98.5, analytics.bestAccuracy, 0.01)
        assertEquals(78.9, analytics.worstAccuracy, 0.01)
        assertEquals(12.5, analytics.wpmImprovement, 0.01)
        assertEquals(5.2, analytics.accuracyImprovement, 0.01)
        assertEquals(150, analytics.totalErrors)
        assertEquals(6.0, analytics.averageErrorsPerTest, 0.01)
    }
    
    @Test
    fun `StatisticsResponse should have correct structure`() {
        val stats = StatisticsResponse(
            testId = "test123",
            profileId = "profile123",
            wpm = 65.5,
            accuracy = 92.3,
            errors = listOf(5, 12, 18),
            timestamp = 1640995200000L
        )
        
        assertEquals("test123", stats.testId)
        assertEquals("profile123", stats.profileId)
        assertEquals(65.5, stats.wpm, 0.01)
        assertEquals(92.3, stats.accuracy, 0.01)
        assertEquals(listOf(5, 12, 18), stats.errors)
        assertEquals(1640995200000L, stats.timestamp)
    }
    
    @Test
    fun `LanguageInfo should have correct structure`() {
        val language = LanguageInfo(
            language = "english",
            dictionaries = listOf("en_US", "en_GB")
        )
        
        assertEquals("english", language.language)
        assertEquals(listOf("en_US", "en_GB"), language.dictionaries)
    }
    
    @Test
    fun `MergerInfo should have correct structure`() {
        val merger = MergerInfo(
            name = "standard",
            description = "Standard word merger"
        )
        
        assertEquals("standard", merger.name)
        assertEquals("Standard word merger", merger.description)
    }
    
    @Test
    fun `ModifierInfo should have correct structure`() {
        val modifier = ModifierInfo(
            name = "punctuation",
            description = "Include punctuation marks"
        )
        
        assertEquals("punctuation", modifier.name)
        assertEquals("Include punctuation marks", modifier.description)
    }
} 