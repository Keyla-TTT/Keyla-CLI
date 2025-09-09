package org.keyla.api

import org.keyla.models.*

/**
 * Service interface for managing user profiles.
 *
 * This service handles all profile-related operations including retrieving existing profiles
 * and creating new user profiles. It provides the core functionality for profile management
 * in the typing test application.
 */
interface ProfileService {
    /**
     * Retrieves all available user profiles.
     *
     * @return A ProfileListResponse containing the list of all user profiles
     */
    suspend fun getAllProfiles(): ProfileListResponse

    /**
     * Creates a new user profile with the specified details.
     *
     * @param request The profile creation request containing user details
     * @return A ProfileResponse containing the created profile information
     */
    suspend fun createProfile(request: CreateProfileRequest): ProfileResponse
}
