package org.keyla.api

import org.keyla.models.AnalyticsResponse

interface AnalyticsService {
    suspend fun getUserAnalytics(userId: String): AnalyticsResponse
} 