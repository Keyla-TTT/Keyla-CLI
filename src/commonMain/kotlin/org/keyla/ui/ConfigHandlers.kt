package org.keyla.ui

import org.keyla.api.ConfigService
import org.keyla.models.*

suspend fun handleViewAllConfig(
    configService: ConfigService,
    state: ConfigState,
    updateState: (ConfigState) -> Unit,
    setScreen: (ConfigScreen) -> Unit,
) {
    try {
        val configList = configService.getAllConfig()
        updateState(state.copy(configList = configList))
        setScreen(ConfigScreen.ViewAllConfig)
    } catch (e: Exception) {
        updateState(state.copy(errorMessage = getErrorMessage(e, "viewAllConfig")))
        setScreen(ConfigScreen.Error)
    }
}

suspend fun handleUpdateConfig(
    configService: ConfigService,
    keyInput: String,
    valueInput: String,
    state: ConfigState,
    updateState: (ConfigState) -> Unit,
    setScreen: (ConfigScreen) -> Unit,
) {
    try {
        val updateRequest = SimpleConfigUpdateRequest(keyInput, valueInput)
        val res = configService.updateConfig(updateRequest)
        updateState(state.copy(successMessage = res.message))
        setScreen(ConfigScreen.Success)
    } catch (e: Exception) {
        updateState(state.copy(errorMessage = getErrorMessage(e, "updateConfig")))
        setScreen(ConfigScreen.Error)
    }
}
