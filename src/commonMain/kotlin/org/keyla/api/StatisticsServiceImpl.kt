package org.keyla.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.keyla.models.ProfileStatisticsListResponse

class StatisticsServiceImpl(
    private val baseUrl: String,
    private val httpClient: HttpClient
) : StatisticsService {
    
    override suspend fun getProfileStatistics(profileId: String): ProfileStatisticsListResponse {
        return httpClient.get("$baseUrl/stats/$profileId").body()
    }

    fun close() {
        httpClient.close()
    }
} 