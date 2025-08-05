package org.keyla.ui

import org.keyla.api.ProfileService
import org.keyla.models.*

suspend fun handleChangeProfile(
    profileService: ProfileService,
    state: ProfileState,
    updateState: (ProfileState) -> Unit,
    setScreen: (ProfileScreen) -> Unit,
) {
    try {
        val profilesResponse = profileService.getAllProfiles()
        if (profilesResponse.profiles.isNotEmpty()) {
            updateState(state.copy(availableProfiles = profilesResponse.profiles))
            setScreen(ProfileScreen.SelectProfile)
        } else {
            updateState(state.copy(infoMessage = "No profiles found. Let's create one!"))
            setScreen(ProfileScreen.CreateProfile)
        }
    } catch (e: Exception) {
        updateState(state.copy(errorMessage = getErrorMessage(e, "changeProfile")))
        setScreen(ProfileScreen.Error)
    }
}

suspend fun handleCreateProfile(
    profileService: ProfileService,
    nameInput: String,
    emailInput: String,
    state: ProfileState,
    updateState: (ProfileState) -> Unit,
    setScreen: (ProfileScreen) -> Unit,
    configurationManager: org.keyla.core.interfaces.ConfigurationManager,
) {
    try {
        val createRequest = CreateProfileRequest(nameInput, emailInput)
        val profile = profileService.createProfile(createRequest)
        configurationManager.setActiveProfileId(profile.id)
        updateState(
            state.copy(
                currentProfile = profile,
                successMessage = "Profile created successfully!",
            ),
        )
        setScreen(ProfileScreen.Success)
    } catch (e: Exception) {
        updateState(state.copy(errorMessage = getErrorMessage(e, "createProfile")))
        setScreen(ProfileScreen.Error)
    }
}
