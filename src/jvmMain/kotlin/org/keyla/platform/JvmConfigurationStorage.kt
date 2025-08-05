package org.keyla.platform

import org.keyla.core.interfaces.ConfigurationStorage
import java.io.File
import java.util.Properties

/**
 * JVM-specific implementation of ConfigurationStorage using Java Properties.
 *
 * This implementation stores configuration in a properties file on the local filesystem.
 * It provides persistent storage across application restarts.
 *
 * @see org.keyla.core.interfaces.ConfigurationStorage
 */
class JvmConfigurationStorage : ConfigurationStorage {
    private val configFile = File("config.properties")
    private val properties = Properties()

    init {
        loadConfig()
    }

    private fun loadConfig() {
        if (configFile.exists()) {
            configFile.inputStream().use { input ->
                properties.load(input)
            }
        } else {
            createDefaultConfig()
        }
    }

    private fun createDefaultConfig() {
        properties.setProperty("api.base.url", "http://localhost:9999/api")
        saveConfig()
    }

    private fun saveConfig() {
        configFile.outputStream().use { output ->
            properties.store(output, "Keyla CLI Configuration")
        }
    }

    override fun loadString(key: String): String? {
        return properties.getProperty(key)
    }

    override fun saveString(
        key: String,
        value: String,
    ) {
        properties.setProperty(key, value)
        saveConfig()
    }

    override fun loadBoolean(key: String): Boolean? {
        val value = properties.getProperty(key)
        return value?.toBoolean()
    }

    override fun saveBoolean(
        key: String,
        value: Boolean,
    ) {
        properties.setProperty(key, value.toString())
        saveConfig()
    }

    override fun loadInt(key: String): Int? {
        val value = properties.getProperty(key)
        return value?.toIntOrNull()
    }

    override fun saveInt(
        key: String,
        value: Int,
    ) {
        properties.setProperty(key, value.toString())
        saveConfig()
    }
}
