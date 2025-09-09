package org.keyla.core

import org.keyla.core.interfaces.*
import kotlin.test.*

class ApplicationContainerTest {
    private lateinit var mockConfigurationStorage: MockConfigurationStorage
    private lateinit var mockPlatformService: MockPlatformService
    private lateinit var container: ApplicationContainer

    @BeforeTest
    fun setup() {
        mockConfigurationStorage = MockConfigurationStorage()
        mockPlatformService = MockPlatformService()

        container =
            ApplicationContainer(
                mockConfigurationStorage,
                mockPlatformService,
            )
    }

    @Test
    fun `getApplication should return TypingTestApplication instance`() {
        val application = container.getApplication()

        assertNotNull(application)
        assertTrue(application is TypingTestApplication)
    }

    @Test
    fun `getConfigurationManager should return AppConfigurationManager instance`() {
        val configManager = container.getConfigurationManager()

        assertNotNull(configManager)
        assertTrue(configManager is AppConfigurationManager)
    }

    @Test
    fun `getApiServiceFactory should return ApiServiceFactory instance`() {
        val apiServiceFactory = container.getApiServiceFactory()

        assertNotNull(apiServiceFactory)
    }

    @Test
    fun `configuration manager should use provided storage`() {
        val configManager = container.getConfigurationManager() as AppConfigurationManager

        assertEquals("http://localhost:9999/api", configManager.getApiBaseUrl())

        configManager.setApiBaseUrl("http://new-api.com/api")
        assertEquals("http://new-api.com/api", configManager.getApiBaseUrl())
        assertTrue(mockConfigurationStorage.saveStringCalled)
    }

    @Test
    fun `api service factory should use configuration manager base URL`() {
        val apiServiceFactory = container.getApiServiceFactory()

        assertNotNull(apiServiceFactory)
    }
}

/**
 * Mock implementations for testing
 */
class MockConfigurationStorage : ConfigurationStorage {
    var saveStringCalled = false

    override fun loadString(key: String): String? {
        return when (key) {
            "api.base.url" -> "http://localhost:9999/api"
            else -> null
        }
    }

    override fun saveString(
        key: String,
        value: String,
    ) {
        saveStringCalled = true
    }

    override fun loadBoolean(key: String): Boolean? = null

    override fun saveBoolean(
        key: String,
        value: Boolean,
    ) {}

    override fun loadInt(key: String): Int? = null

    override fun saveInt(
        key: String,
        value: Int,
    ) {}
}

class MockPlatformService : PlatformService {
    override fun exitProcess(code: Int): Nothing {
        throw RuntimeException("Mock exit with code: $code")
    }

    override fun getCurrentTimeMillis(): Long = 1234567890L

    override fun getUserInput(): String? = null
}
