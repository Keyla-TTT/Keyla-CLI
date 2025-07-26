package org.keyla.api

class ApiServiceFactoryImpl(
    private val baseUrl: String
) : ApiServiceFactory {
    private val httpClient = createHttpClient()

    override fun createTestService(): TestService {
        return TestServiceImpl(baseUrl, httpClient)
    }

    override fun createProfileService(): ProfileService {
        return ProfileServiceImpl(baseUrl, httpClient)
    }

    override fun createConfigService(): ConfigService {
        return ConfigServiceImpl(baseUrl, httpClient)
    }

    override fun createLanguageService(): LanguageService {
        return LanguageServiceImpl(baseUrl, httpClient)
    }

    override fun createAnalyticsService(): AnalyticsService {
        return AnalyticsServiceImpl(baseUrl, httpClient)
    }

    override fun createStatisticsService(): StatisticsService {
        return StatisticsServiceImpl(baseUrl, httpClient)
    }

    override fun close() {
        httpClient.close()
    }
} 