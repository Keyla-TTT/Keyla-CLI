package org.keyla.api

interface ApiServiceFactory {
    fun createTestService(): TestService
    fun createProfileService(): ProfileService
    fun createConfigService(): ConfigService
    fun createLanguageService(): LanguageService
    fun createAnalyticsService(): AnalyticsService
    fun createStatisticsService(): StatisticsService
    fun close()
} 