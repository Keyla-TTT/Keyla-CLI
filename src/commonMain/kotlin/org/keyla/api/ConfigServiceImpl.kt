package org.keyla.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.keyla.models.*

class ConfigServiceImpl(
    private val baseUrl: String,
    private val httpClient: HttpClient
) : ConfigService {
    override suspend fun getAllConfig(): ConfigListResponse {
        return httpClient.get("$baseUrl/config").body()
    }

    override suspend fun getCurrentConfig(): AppConfig {
        return httpClient.get("$baseUrl/current-config").body()
    }

    override suspend fun getConfigEntry(section: String, key: String): ConfigEntry {
        return httpClient.get("$baseUrl/config/$section/$key").body()
    }

    override suspend fun updateConfig(request: SimpleConfigUpdateRequest): ConfigUpdateResponse {
        return httpClient.put("$baseUrl/config") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun reloadConfig(): AppConfig {
        return httpClient.post("$baseUrl/config/reload").body()
    }

    override suspend fun resetConfig(): AppConfig {
        return httpClient.post("$baseUrl/config/reset").body()
    }

    override suspend fun testConnection(): Boolean {
        return try {
            val url = "$baseUrl/current-config"
            val result = httpClient.get(url).status.isSuccess()
            result
        } catch (e: Exception) {
            return false
        }
    }

    fun close() {
        httpClient.close()
    }
} 