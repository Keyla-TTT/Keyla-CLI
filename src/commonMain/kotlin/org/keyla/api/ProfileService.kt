package org.keyla.api

import org.keyla.models.*

interface ProfileService {
    suspend fun getAllProfiles(): ProfileListResponse

    suspend fun createProfile(request: CreateProfileRequest): ProfileResponse
}
