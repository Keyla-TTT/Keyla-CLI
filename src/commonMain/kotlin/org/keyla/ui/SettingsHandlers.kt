package org.keyla.ui

import org.keyla.api.ConfigService
import org.keyla.api.ProfileService
import org.keyla.core.interfaces.ConfigurationManager

suspend fun handleUpdateBackendUrl(
    newUrl: String,
    state: SettingsState,
    updateState: (SettingsState) -> Unit,
    setScreen: (SettingsScreen) -> Unit,
    configurationManager: ConfigurationManager,
) {
    try {
        configurationManager.setApiBaseUrl(newUrl)
        updateState(
            state.copy(
                currentBackendUrl = newUrl,
                successMessage = "Backend URL updated successfully!",
            ),
        )
        setScreen(SettingsScreen.Success)
    } catch (e: Exception) {
        updateState(state.copy(errorMessage = getErrorMessage(e, "updateBackendUrl")))
        setScreen(SettingsScreen.Error)
    }
}

suspend fun handleTestConnection(
    configService: ConfigService,
    state: SettingsState,
    updateState: (SettingsState) -> Unit,
    setScreen: (SettingsScreen) -> Unit,
) {
    try {
        val isConnected = configService.testConnection()
        if (isConnected) {
            updateState(state.copy(successMessage = "Connection test successful!"))
            setScreen(SettingsScreen.Success)
        } else {
            updateState(state.copy(errorMessage = "Connection to ${state.currentBackendUrl} failed :("))
            setScreen(SettingsScreen.Error)
        }
    } catch (e: Exception) {
        updateState(state.copy(errorMessage = "Connection to ${state.currentBackendUrl} failed :("))
        setScreen(SettingsScreen.Error)
    }
}

suspend fun handleLoadProfiles(
    profileService: ProfileService,
    state: SettingsState,
    updateState: (SettingsState) -> Unit,
    setScreen: (SettingsScreen) -> Unit,
) {
    try {
        val profilesResponse = profileService.getAllProfiles()
        updateState(state.copy(availableProfiles = profilesResponse.profiles))
        setScreen(SettingsScreen.SelectProfile)
    } catch (e: Exception) {
        updateState(state.copy(errorMessage = getErrorMessage(e, "loadProfiles")))
        setScreen(SettingsScreen.Error)
    }
}

suspend fun handleSelectProfile(
    profileService: ProfileService,
    state: SettingsState,
    profileIndex: Int,
    updateState: (SettingsState) -> Unit,
    setScreen: (SettingsScreen) -> Unit,
    configurationManager: ConfigurationManager,
) {
    try {
        val selectedProfile = state.availableProfiles[profileIndex]
        configurationManager.setActiveProfileId(selectedProfile.id)
        updateState(
            state.copy(
                currentProfile = selectedProfile,
                successMessage = "Active profile changed to ${selectedProfile.name}!",
            ),
        )
        setScreen(SettingsScreen.Success)
    } catch (e: Exception) {
        updateState(state.copy(errorMessage = getErrorMessage(e, "selectProfile")))
        setScreen(SettingsScreen.Error)
    }
}
