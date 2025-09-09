package org.keyla.api

/**
 * Factory interface for creating API service instances.
 *
 * This factory provides methods to create various API services used throughout the application.
 * It centralizes service creation and ensures proper initialization of all API dependencies.
 * The factory should be closed when no longer needed to properly clean up resources.
 */
interface ApiServiceFactory {
    /**
     * Creates a new instance of the TestService.
     *
     * @return A configured TestService instance for managing typing tests
     */
    fun createTestService(): TestService

    /**
     * Creates a new instance of the ProfileService.
     *
     * @return A configured ProfileService instance for managing user profiles
     */
    fun createProfileService(): ProfileService

    /**
     * Creates a new instance of the ConfigService.
     *
     * @return A configured ConfigService instance for managing application configuration
     */
    fun createConfigService(): ConfigService

    /**
     * Creates a new instance of the LanguageService.
     *
     * @return A configured LanguageService instance for managing language data
     */
    fun createLanguageService(): LanguageService

    /**
     * Creates a new instance of the AnalyticsService.
     *
     * @return A configured AnalyticsService instance for retrieving user analytics
     */
    fun createAnalyticsService(): AnalyticsService

    /**
     * Creates a new instance of the StatisticsService.
     *
     * @return A configured StatisticsService instance for retrieving user statistics
     */
    fun createStatisticsService(): StatisticsService

    /**
     * Closes the factory and releases all associated resources.
     *
     * This method should be called when the factory is no longer needed to ensure
     * proper cleanup of resources such as HTTP clients and database connections.
     */
    fun close()
}
