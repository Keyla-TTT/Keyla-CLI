package org.keyla.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.keyla.models.*

class LanguageServiceImpl(
    private val baseUrl: String,
    private val httpClient: HttpClient,
) : LanguageService {
    override suspend fun getDictionaries(): DictionariesResponse {
        return try {
            httpClient.get("$baseUrl/dictionaries").body()
        } catch (e: Exception) {
            DictionariesResponse(emptyList())
        }
    }

    override suspend fun getMergers(): MergersResponse {
        return try {
            httpClient.get("$baseUrl/mergers").body()
        } catch (e: Exception) {
            MergersResponse(emptyList())
        }
    }

    override suspend fun getModifiers(): ModifiersResponse {
        return try {
            httpClient.get("$baseUrl/modifiers").body()
        } catch (e: Exception) {
            ModifiersResponse(emptyList())
        }
    }

    fun close() {
        httpClient.close()
    }
}
