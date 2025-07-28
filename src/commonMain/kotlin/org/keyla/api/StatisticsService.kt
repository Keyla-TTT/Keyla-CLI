package org.keyla.api

import org.keyla.models.ProfileStatisticsListResponse

interface StatisticsService {
    suspend fun getProfileStatistics(profileId: String): ProfileStatisticsListResponse
} 