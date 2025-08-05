package org.keyla.core.interfaces

interface ConfigurationManager {
    fun getApiBaseUrl(): String

    fun setApiBaseUrl(url: String)

    fun getActiveProfileId(): String?

    fun setActiveProfileId(profileId: String)

    fun loadConfiguration()

    fun saveConfiguration()
}
