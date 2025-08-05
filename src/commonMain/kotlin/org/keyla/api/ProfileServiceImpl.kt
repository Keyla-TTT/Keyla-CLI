package org.keyla.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.keyla.models.*

class ProfileServiceImpl(
    private val baseUrl: String,
    private val httpClient: HttpClient,
) : ProfileService {
    override suspend fun getAllProfiles(): ProfileListResponse {
        return try {
            httpClient.get("$baseUrl/profiles").body()
        } catch (e: Exception) {
            return ProfileListResponse(profiles = emptyList())
        }
    }

    override suspend fun createProfile(request: CreateProfileRequest): ProfileResponse {
        return httpClient.post("$baseUrl/profiles") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    fun close() {
        httpClient.close()
    }
}
