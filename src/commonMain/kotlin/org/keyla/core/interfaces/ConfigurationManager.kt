package org.keyla.core.interfaces

/**
 * Interface for managing application configuration.
 *
 * This interface provides methods to manage application settings including API configuration
 * and active profile selection. It handles loading and saving configuration data and provides
 * access to key configuration values used throughout the application.
 */
interface ConfigurationManager {
    /**
     * Retrieves the current API base URL configuration.
     *
     * @return The configured API base URL as a string
     */
    fun getApiBaseUrl(): String

    /**
     * Sets the API base URL configuration.
     *
     * @param url The new API base URL to set
     */
    fun setApiBaseUrl(url: String)

    /**
     * Retrieves the currently active profile ID.
     *
     * @return The active profile ID, or null if no profile is active
     */
    fun getActiveProfileId(): String?

    /**
     * Sets the active profile ID.
     *
     * @param profileId The ID of the profile to set as active
     */
    fun setActiveProfileId(profileId: String)

    /**
     * Loads configuration from the persistent storage.
     *
     * This method reads all configuration values from the storage backend
     * and updates the in-memory configuration state.
     */
    fun loadConfiguration()

    /**
     * Saves the current configuration to persistent storage.
     *
     * This method persists all current configuration values to the storage backend.
     */
    fun saveConfiguration()
}
