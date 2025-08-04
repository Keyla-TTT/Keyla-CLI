package org.keyla.api

import kotlin.test.*

class ApiServiceFactoryTest {
    private lateinit var apiServiceFactory: ApiServiceFactoryImpl

    @BeforeTest
    fun setup() {
        apiServiceFactory = ApiServiceFactoryImpl("http://localhost:8080/api")
    }

    @AfterTest
    fun tearDown() {
        apiServiceFactory.close()
    }

    @Test
    fun `createTestService should return TestServiceImpl instance`() {
        val testService = apiServiceFactory.createTestService()

        assertNotNull(testService)
        assertTrue(testService is TestServiceImpl)
    }

    @Test
    fun `createProfileService should return ProfileServiceImpl instance`() {
        val profileService = apiServiceFactory.createProfileService()

        assertNotNull(profileService)
        assertTrue(profileService is ProfileServiceImpl)
    }

    @Test
    fun `createConfigService should return ConfigServiceImpl instance`() {
        val configService = apiServiceFactory.createConfigService()

        assertNotNull(configService)
        assertTrue(configService is ConfigServiceImpl)
    }

    @Test
    fun `createLanguageService should return LanguageServiceImpl instance`() {
        val languageService = apiServiceFactory.createLanguageService()

        assertNotNull(languageService)
        assertTrue(languageService is LanguageServiceImpl)
    }

    @Test
    fun `createAnalyticsService should return AnalyticsServiceImpl instance`() {
        val analyticsService = apiServiceFactory.createAnalyticsService()

        assertNotNull(analyticsService)
        assertTrue(analyticsService is AnalyticsServiceImpl)
    }

    @Test
    fun `createStatisticsService should return StatisticsServiceImpl instance`() {
        val statisticsService = apiServiceFactory.createStatisticsService()

        assertNotNull(statisticsService)
        assertTrue(statisticsService is StatisticsServiceImpl)
    }

    @Test
    fun `close should not throw exception`() {
        apiServiceFactory.close()
    }
}
