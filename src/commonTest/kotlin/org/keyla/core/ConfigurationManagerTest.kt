package org.keyla.core

import kotlin.test.*

class ConfigurationManagerTest {
    private lateinit var mockStorage: MockConfigurationStorage
    private lateinit var configManager: AppConfigurationManager

    @BeforeTest
    fun setup() {
        mockStorage = MockConfigurationStorage()
        configManager = AppConfigurationManager(mockStorage)
    }

    @Test
    fun `getApiBaseUrl should return default URL initially`() {
        val url = configManager.getApiBaseUrl()

        assertEquals("http://localhost:9999/api", url)
    }

    @Test
    fun `setApiBaseUrl should update the URL and save to storage`() {
        val newUrl = "http://new-api.com/api"

        configManager.setApiBaseUrl(newUrl)

        assertEquals(newUrl, configManager.getApiBaseUrl())
        assertTrue(mockStorage.saveStringCalled)
    }

    @Test
    fun `getActiveProfileId should return null initially`() {
        val profileId = configManager.getActiveProfileId()

        assertNull(profileId)
    }

    @Test
    fun `setActiveProfileId should update the profile ID and save to storage`() {
        val profileId = "profile123"

        configManager.setActiveProfileId(profileId)

        assertEquals(profileId, configManager.getActiveProfileId())
        assertTrue(mockStorage.saveStringCalled)
    }

    @Test
    fun `loadConfiguration should load values from storage`() {
        configManager.loadConfiguration()

        assertEquals("http://localhost:9999/api", configManager.getApiBaseUrl())
        assertNull(configManager.getActiveProfileId())
    }

    @Test
    fun `saveConfiguration should save current values to storage`() {
        configManager.setApiBaseUrl("http://test-api.com/api")
        configManager.setActiveProfileId("test-profile")

        mockStorage.saveStringCalled = false

        configManager.saveConfiguration()

        assertTrue(mockStorage.saveStringCalled)
    }
}
