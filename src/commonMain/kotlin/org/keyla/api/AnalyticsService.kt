package org.keyla.api

import org.keyla.models.AnalyticsResponse

/**
 * Service interface for retrieving user analytics data.
 *
 * This service provides access to detailed analytics and insights about user
 * behavior and performance patterns. It aggregates data from various sources
 * to provide comprehensive analytics reports for individual users.
 */
interface AnalyticsService {
    /**
     * Retrieves detailed analytics data for a specific user.
     *
     * @param userId The ID of the user to get analytics for
     * @return An AnalyticsResponse containing comprehensive analytics data
     */
    suspend fun getUserAnalytics(userId: String): AnalyticsResponse
}
