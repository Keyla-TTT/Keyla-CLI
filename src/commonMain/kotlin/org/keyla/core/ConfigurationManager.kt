package org.keyla.core

import org.keyla.core.interfaces.ConfigurationManager
import org.keyla.core.interfaces.ConfigurationStorage

class AppConfigurationManager(
    private val storage: ConfigurationStorage,
) : ConfigurationManager {
    private var apiBaseUrl: String = "http://localhost:9999/api"
    private var activeProfileId: String? = null

    init {
        loadConfiguration()
    }

    override fun getApiBaseUrl(): String = apiBaseUrl

    override fun setApiBaseUrl(url: String) {
        apiBaseUrl = url
        saveConfiguration()
    }

    override fun getActiveProfileId(): String? = activeProfileId

    override fun setActiveProfileId(profileId: String) {
        activeProfileId = profileId
        saveConfiguration()
    }

    override fun loadConfiguration() {
        apiBaseUrl = storage.loadString("api.base.url") ?: "http://localhost:9999/api"
        activeProfileId = storage.loadString("active.profile.id")
    }

    override fun saveConfiguration() {
        storage.saveString("api.base.url", apiBaseUrl)
        activeProfileId?.let { storage.saveString("active.profile.id", it) }
    }
}
