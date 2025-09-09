package org.keyla.api

import org.keyla.models.ProfileStatisticsListResponse

/**
 * Service interface for retrieving user statistics.
 *
 * This service provides access to user performance statistics and analytics data.
 * It retrieves comprehensive statistics for specific user profiles, enabling
 * users to track their typing performance and progress over time.
 */
interface StatisticsService {
    /**
     * Retrieves comprehensive statistics for a specific user profile.
     *
     * @param profileId The ID of the profile to get statistics for
     * @return A ProfileStatisticsListResponse containing all statistics for the profile
     */
    suspend fun getProfileStatistics(profileId: String): ProfileStatisticsListResponse
}
