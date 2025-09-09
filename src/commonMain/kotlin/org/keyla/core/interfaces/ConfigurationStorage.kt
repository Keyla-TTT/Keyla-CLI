package org.keyla.core.interfaces

/**
 * Interface for persistent configuration storage.
 *
 * This interface defines methods for storing and retrieving configuration data
 * in a persistent manner. It supports different data types including strings,
 * booleans, and integers, providing a flexible storage mechanism for application
 * settings and user preferences.
 */
interface ConfigurationStorage {
    /**
     * Loads a string value from persistent storage.
     *
     * @param key The configuration key to retrieve
     * @return The stored string value, or null if the key doesn't exist
     */
    fun loadString(key: String): String?

    /**
     * Saves a string value to persistent storage.
     *
     * @param key The configuration key to store
     * @param value The string value to store
     */
    fun saveString(
        key: String,
        value: String,
    )

    /**
     * Loads a boolean value from persistent storage.
     *
     * @param key The configuration key to retrieve
     * @return The stored boolean value, or null if the key doesn't exist
     */
    fun loadBoolean(key: String): Boolean?

    /**
     * Saves a boolean value to persistent storage.
     *
     * @param key The configuration key to store
     * @param value The boolean value to store
     */
    fun saveBoolean(
        key: String,
        value: Boolean,
    )

    /**
     * Loads an integer value from persistent storage.
     *
     * @param key The configuration key to retrieve
     * @return The stored integer value, or null if the key doesn't exist
     */
    fun loadInt(key: String): Int?

    /**
     * Saves an integer value to persistent storage.
     *
     * @param key The configuration key to store
     * @param value The integer value to store
     */
    fun saveInt(
        key: String,
        value: Int,
    )
}
