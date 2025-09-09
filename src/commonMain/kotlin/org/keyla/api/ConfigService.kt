package org.keyla.api

import org.keyla.models.*

/**
 * Service interface for managing application configuration via API.
 *
 * This service provides remote configuration management capabilities including
 * retrieving configuration settings, updating specific configuration entries,
 * and testing API connectivity. It handles configuration synchronization
 * between the local application and remote configuration sources.
 */
interface ConfigService {
    /**
     * Retrieves all available configuration entries.
     *
     * @return A ConfigListResponse containing all configuration entries
     */
    suspend fun getAllConfig(): ConfigListResponse

    /**
     * Retrieves the current application configuration.
     *
     * @return An AppConfig containing the current configuration state
     */
    suspend fun getCurrentConfig(): AppConfig

    /**
     * Retrieves a specific configuration entry by section and key.
     *
     * @param section The configuration section name
     * @param key The configuration key within the section
     * @return A ConfigEntry containing the requested configuration value
     */
    suspend fun getConfigEntry(
        section: String,
        key: String,
    ): ConfigEntry

    /**
     * Updates a configuration entry with new values.
     *
     * @param request The configuration update request containing new values
     * @return A ConfigUpdateResponse indicating the success of the update operation
     */
    suspend fun updateConfig(request: SimpleConfigUpdateRequest): ConfigUpdateResponse

    /**
     * Reloads the configuration from the remote source.
     *
     * @return An AppConfig containing the reloaded configuration
     */
    suspend fun reloadConfig(): AppConfig

    /**
     * Resets the configuration to default values.
     *
     * @return An AppConfig containing the reset configuration
     */
    suspend fun resetConfig(): AppConfig

    /**
     * Tests the connection to the configuration service.
     *
     * @return true if the connection is successful, false otherwise
     */
    suspend fun testConnection(): Boolean
}
