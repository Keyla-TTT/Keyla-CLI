package org.keyla.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.keyla.models.AnalyticsResponse

class AnalyticsServiceImpl(
    private val baseUrl: String,
    private val httpClient: HttpClient,
) : AnalyticsService {
    override suspend fun getUserAnalytics(userId: String): AnalyticsResponse {
        return httpClient.get("$baseUrl/analytics/$userId").body()
    }
}
