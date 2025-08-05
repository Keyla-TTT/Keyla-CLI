package org.keyla.ui

import com.varabyte.kotter.foundation.input.name
import com.varabyte.kotter.foundation.input.onKeyPressed
import com.varabyte.kotter.foundation.liveVarOf
import com.varabyte.kotter.foundation.runUntilSignal
import com.varabyte.kotter.foundation.session
import com.varabyte.kotter.foundation.text.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.keyla.models.*

sealed class SettingsScreen {
    object MainMenu : SettingsScreen()

    object UpdateBackendUrl : SettingsScreen()

    object TestConnection : SettingsScreen()

    object SelectProfile : SettingsScreen()

    object ProfileInfo : SettingsScreen()

    object Error : SettingsScreen()

    object Success : SettingsScreen()
}

data class SettingsState(
    val currentBackendUrl: String = "",
    val currentProfile: ProfileResponse? = null,
    val availableProfiles: List<ProfileResponse> = emptyList(),
    val errorMessage: String? = null,
    val successMessage: String? = null,
)

suspend fun settingsMode(
    configService: org.keyla.api.ConfigService,
    profileService: org.keyla.api.ProfileService,
    configurationManager: org.keyla.core.interfaces.ConfigurationManager,
) {
    coroutineScope {
        session {
            var screen by liveVarOf<SettingsScreen>(SettingsScreen.MainMenu)
            var state by liveVarOf(SettingsState(currentBackendUrl = configurationManager.getApiBaseUrl()))

            var settingsMenuIndex by liveVarOf(0)
            var profileIndex by liveVarOf(0)
            var focusedField by liveVarOf(0)

            var backendUrlInput by liveVarOf("")

            fun updateState(newState: SettingsState) {
                state = newState
            }

            fun setScreen(newScreen: SettingsScreen) {
                screen = newScreen
            }

            launch {
                try {
                    val profilesResponse = profileService.getAllProfiles()
                    val activeProfileId = configurationManager.getActiveProfileId()
                    val currentProfile =
                        if (activeProfileId != null) {
                            profilesResponse.profiles.find { it.id == activeProfileId }
                        } else {
                            profilesResponse.profiles.firstOrNull()
                        }
                    updateState(
                        state.copy(
                            availableProfiles = profilesResponse.profiles,
                            currentProfile = currentProfile,
                        ),
                    )
                } catch (e: Exception) {
                    updateState(state.copy(errorMessage = "Failed to load profiles: ${e.message}"))
                }
            }

            section {
                fun renderHeader(title: String) {
                    green { textLine("╔══════════════════════════════════════════════════════════════╗") }
                    green { textLine("║                    $title                    ║") }
                    green { textLine("╚══════════════════════════════════════════════════════════════╝") }
                    textLine()
                }

                fun renderGoBackOption() {
                    textLine()
                    cyan { textLine("> Go Back") }
                    textLine()
                    yellow { textLine("Press Enter to go back") }
                }

                when (screen) {
                    is SettingsScreen.MainMenu -> {
                        renderHeader("CLI SETTINGS")
                        textLine("Current Backend URL: ${state.currentBackendUrl}")
                        textLine("Current Active Profile: ${state.currentProfile?.name ?: "None"}")
                        textLine()
                        val options =
                            listOf(
                                "1. Update Backend URL",
                                "2. Change Active Profile",
                                "3. Test Connection",
                                "4. Exit",
                            )
                        options.forEachIndexed { i, opt ->
                            if (i == settingsMenuIndex) {
                                cyan { textLine("> $opt") }
                            } else {
                                textLine("  $opt")
                            }
                        }
                        textLine()
                        yellow { textLine("Use arrow keys to navigate, Enter to select") }
                    }
                    is SettingsScreen.UpdateBackendUrl -> {
                        renderHeader("UPDATE BACKEND URL")
                        textLine("Current URL: ${state.currentBackendUrl}")
                        textLine("New URL: $backendUrlInput")
                        textLine()
                        when (focusedField) {
                            0 -> yellow { textLine("Enter new backend URL (e.g., http://localhost:8080): ") }
                            1 -> yellow { textLine("Go Back") }
                        }
                        textLine("Press Tab to switch fields, Enter to confirm")
                    }
                    is SettingsScreen.SelectProfile -> {
                        renderHeader("SELECT ACTIVE PROFILE")
                        if (state.availableProfiles.isEmpty()) {
                            textLine("No profiles available.")
                            textLine("Please create a profile first using 'keyla profile'")
                        } else {
                            state.availableProfiles.forEachIndexed { i, profile ->
                                if (i == profileIndex) {
                                    cyan { textLine("> ${profile.name} (${profile.email})") }
                                } else {
                                    textLine("  ${profile.name} (${profile.email})")
                                }
                            }
                        }
                        textLine()
                        if (profileIndex == state.availableProfiles.size) {
                            cyan { textLine("> Go Back") }
                        } else {
                            textLine("  Go Back")
                        }
                        textLine()
                        yellow { textLine("Use arrow keys to navigate, Enter to select") }
                    }
                    is SettingsScreen.ProfileInfo -> {
                        renderHeader("PROFILE INFORMATION")
                        state.currentProfile?.let { profile ->
                            textLine("Active Profile: ${profile.name}")
                            textLine("Email: ${profile.email}")
                            textLine("ID: ${profile.id}")
                        } ?: textLine("No active profile selected")
                        renderGoBackOption()
                    }
                    is SettingsScreen.TestConnection -> {
                        renderHeader("TEST CONNECTION")
                        yellow { textLine("Testing connection to ${state.currentBackendUrl}...") }
                        textLine()
                        if (state.successMessage != null) {
                            green { textLine("✓ Connection successful!") }
                        } else if (state.errorMessage != null) {
                            red { textLine("✗ Connection failed!") }
                        } else {
                            yellow { textLine("Press Enter to start the connection test...") }
                        }
                        renderGoBackOption()
                    }
                    is SettingsScreen.Error -> {
                        red { textLine("ERROR: ${state.errorMessage}") }
                        renderGoBackOption()
                    }
                    is SettingsScreen.Success -> {
                        green { textLine("SUCCESS: ${state.successMessage}") }
                        renderGoBackOption()
                    }
                }
            }.runUntilSignal {
                onKeyPressed {
                    when (screen) {
                        is SettingsScreen.MainMenu -> {
                            when (key.name) {
                                "UP" -> {
                                    settingsMenuIndex = (settingsMenuIndex - 1).coerceAtLeast(0)
                                }
                                "DOWN" -> {
                                    settingsMenuIndex = (settingsMenuIndex + 1).coerceAtMost(3)
                                }
                                "ENTER" -> {
                                    when (settingsMenuIndex) {
                                        0 -> screen = SettingsScreen.UpdateBackendUrl
                                        1 -> {
                                            screen = SettingsScreen.SelectProfile
                                            launch {
                                                handleLoadProfiles(profileService, state, ::updateState, ::setScreen)
                                            }
                                        }
                                        2 -> {
                                            screen = SettingsScreen.TestConnection
                                            launch {
                                                val currentUrl = configurationManager.getApiBaseUrl()
                                                val currentConfigService =
                                                    org.keyla.api.ConfigServiceImpl(
                                                        currentUrl,
                                                        org.keyla.api.createHttpClient(),
                                                    )
                                                handleTestConnection(currentConfigService, state, ::updateState, ::setScreen)
                                            }
                                        }
                                        3 -> signal()
                                    }
                                }
                            }
                        }
                        is SettingsScreen.UpdateBackendUrl -> {
                            when (key.name) {
                                "TAB" -> focusedField = (focusedField + 1) % 2
                                "ENTER" -> {
                                    if (focusedField == 1) {
                                        screen = SettingsScreen.MainMenu
                                    } else if (backendUrlInput.isNotBlank()) {
                                        launch {
                                            handleUpdateBackendUrl(backendUrlInput, state, ::updateState, ::setScreen, configurationManager)
                                        }
                                    }
                                }
                                "BACKSPACE" -> if (backendUrlInput.isNotEmpty()) backendUrlInput = backendUrlInput.dropLast(1)
                                else ->
                                    if (key.name.length == 1) {
                                        backendUrlInput += key.name
                                    }
                            }
                        }
                        is SettingsScreen.SelectProfile -> {
                            when (key.name) {
                                "UP" -> profileIndex = (profileIndex - 1).coerceAtLeast(0)
                                "DOWN" -> profileIndex = (profileIndex + 1).coerceAtMost(state.availableProfiles.size)
                                "ENTER" -> {
                                    if (profileIndex == state.availableProfiles.size) {
                                        screen = SettingsScreen.MainMenu
                                    } else {
                                        launch {
                                            handleSelectProfile(
                                                profileService,
                                                state,
                                                profileIndex,
                                                ::updateState,
                                                ::setScreen,
                                                configurationManager,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        is SettingsScreen.ProfileInfo -> {
                            when (key.name) {
                                "ENTER" -> screen = SettingsScreen.MainMenu
                                else -> screen = SettingsScreen.MainMenu
                            }
                        }
                        is SettingsScreen.TestConnection -> {
                            when (key.name) {
                                "ENTER" -> screen = SettingsScreen.MainMenu
                                else -> screen = SettingsScreen.MainMenu
                            }
                        }
                        is SettingsScreen.Error, is SettingsScreen.Success -> {
                            when (key.name) {
                                "ENTER" -> screen = SettingsScreen.MainMenu
                                else -> screen = SettingsScreen.MainMenu
                            }
                        }
                    }
                }
            }
        }
    }
}
