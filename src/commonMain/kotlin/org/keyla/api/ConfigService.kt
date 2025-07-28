package org.keyla.api

import org.keyla.models.*

interface ConfigService {
    suspend fun getAllConfig(): ConfigListResponse
    suspend fun getCurrentConfig(): AppConfig
    suspend fun getConfigEntry(section: String, key: String): ConfigEntry
    suspend fun updateConfig(request: SimpleConfigUpdateRequest): ConfigUpdateResponse
    suspend fun reloadConfig(): AppConfig
    suspend fun resetConfig(): AppConfig
    suspend fun testConnection(): Boolean
} 