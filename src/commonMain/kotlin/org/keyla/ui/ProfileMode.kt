package org.keyla.ui

import com.varabyte.kotter.foundation.input.onKeyPressed
import com.varabyte.kotter.foundation.liveVarOf
import com.varabyte.kotter.foundation.runUntilSignal
import com.varabyte.kotter.foundation.session
import com.varabyte.kotter.foundation.text.*
import com.varabyte.kotter.foundation.input.name
import org.keyla.models.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

sealed class ProfileScreen {
    object MainMenu : ProfileScreen()
    object SelectProfile : ProfileScreen()
    object CreateProfile : ProfileScreen()
    object Error : ProfileScreen()
    object Success : ProfileScreen()
    object Info : ProfileScreen()
}

data class ProfileState(
    val currentProfile: ProfileResponse? = null,
    val availableProfiles: List<ProfileResponse> = emptyList(),
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val infoMessage: String? = null
)

suspend fun profileMode(
    profileService: org.keyla.api.ProfileService,
    initialProfile: ProfileResponse?,
    configurationManager: org.keyla.core.interfaces.ConfigurationManager
) {
    coroutineScope {
        session {
            var screen by liveVarOf<ProfileScreen>(ProfileScreen.MainMenu)
            var state by liveVarOf(ProfileState())
            
            var profileMenuIndex by liveVarOf(0)
            var profileIndex by liveVarOf(0)
            var focusedField by liveVarOf(0)
            
            var nameInput by liveVarOf("")
            var emailInput by liveVarOf("")

            fun updateState(newState: ProfileState) { state = newState }
            fun setScreen(newScreen: ProfileScreen) { screen = newScreen }

            launch {
                try {
                    val profilesResponse = profileService.getAllProfiles()
                    val activeProfileId = configurationManager.getActiveProfileId()
                    val currentProfile = if (activeProfileId != null) {
                        profilesResponse.profiles.find { it.id == activeProfileId }
                    } else {
                        initialProfile ?: profilesResponse.profiles.firstOrNull()
                    }
                    updateState(state.copy(
                        currentProfile = currentProfile,
                        availableProfiles = profilesResponse.profiles
                    ))
                } catch (e: Exception) {
                    updateState(state.copy(errorMessage = "Failed to load profiles: ${e.message}"))
                }
            }

            section {
                fun renderHeader(title: String) {
                    green { textLine("╔══════════════════════════════════════════════════════════════╗") }
                    green { textLine("║                        $title                    ║") }
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
                    is ProfileScreen.MainMenu -> {
                        renderHeader("PROFILE MANAGEMENT")
                        textLine("Current Profile: ${state.currentProfile?.name ?: "None"}")
                        textLine()
                        val options = listOf(
                            "1. Change Profile",
                            "2. Create New Profile",
                            "3. Exit"
                        )
                        options.forEachIndexed { i, opt ->
                            if (i == profileMenuIndex) cyan { textLine("> $opt") }
                            else textLine("  $opt")
                        }
                        textLine()
                        yellow { textLine("Use arrow keys to navigate, Enter to select") }
                    }
                    is ProfileScreen.SelectProfile -> {
                        renderHeader("SELECT PROFILE")
                        state.availableProfiles.forEachIndexed { i, profile ->
                            if (i == profileIndex) cyan { textLine("> ${profile.name} (${profile.email})") }
                            else textLine("  ${profile.name} (${profile.email})")
                        }
                        textLine()
                        if (profileIndex == state.availableProfiles.size) cyan { textLine("> Create New Profile") }
                        else textLine("  Create New Profile")
                        renderGoBackOption()
                    }
                    is ProfileScreen.CreateProfile -> {
                        renderHeader("PROFILE SETUP")
                        textLine("Create a new profile!")
                        textLine()
                        textLine("Name: $nameInput")
                        textLine("Email: $emailInput")
                        textLine()
                        yellow { textLine("Fill in your details and press Enter to create profile") }
                        textLine("Press Tab to switch fields, or select Go Back to return")
                        textLine()
                        if (focusedField == 2) cyan { textLine("> Go Back") }
                        else textLine("  Go Back")
                    }
                    is ProfileScreen.Error -> {
                        red { textLine("ERROR: ${state.errorMessage}") }
                        renderGoBackOption()
                    }
                    is ProfileScreen.Success -> {
                        green { textLine("SUCCESS: ${state.successMessage}") }
                        renderGoBackOption()
                    }
                    is ProfileScreen.Info -> {
                        cyan { textLine("INFO: ${state.infoMessage}") }
                        renderGoBackOption()
                    }
                }
            }.runUntilSignal {
                onKeyPressed {
                    when (screen) {
                        is ProfileScreen.MainMenu -> {
                            when (key.name) {
                                "UP" -> { profileMenuIndex = (profileMenuIndex - 1).coerceAtLeast(0) }
                                "DOWN" -> { profileMenuIndex = (profileMenuIndex + 1).coerceAtMost(2) }
                                "ENTER" -> {
                                    when (profileMenuIndex) {
                                        0 -> launch { handleChangeProfile(profileService, state, ::updateState, ::setScreen) }
                                        1 -> {
                                            nameInput = ""
                                            emailInput = ""
                                            focusedField = 0
                                            screen = ProfileScreen.CreateProfile
                                        }
                                        2 -> signal()
                                    }
                                }
                            }
                        }
                        is ProfileScreen.SelectProfile -> {
                            when (key.name) {
                                "UP" -> { profileIndex = (profileIndex - 1).coerceAtLeast(0) }
                                "DOWN" -> { profileIndex = (profileIndex + 1).coerceAtMost(state.availableProfiles.size + 1) }
                                "ENTER" -> {
                                    if (profileIndex < state.availableProfiles.size) {
                                        val selectedProfile = state.availableProfiles[profileIndex]
                                        configurationManager.setActiveProfileId(selectedProfile.id)
                                        state = state.copy(currentProfile = selectedProfile)
                                        screen = ProfileScreen.MainMenu
                                    } else if (profileIndex == state.availableProfiles.size) {
                                        nameInput = ""
                                        emailInput = ""
                                        focusedField = 0
                                        screen = ProfileScreen.CreateProfile
                                    } else if (profileIndex == state.availableProfiles.size + 1) {
                                        screen = ProfileScreen.MainMenu
                                    }
                                }
                            }
                        }
                        is ProfileScreen.CreateProfile -> {
                            when (key.name) {
                                "TAB" -> {
                                    focusedField = (focusedField + 1) % 3
                                }
                                "ENTER" -> {
                                    if (focusedField == 2) {
                                        screen = ProfileScreen.MainMenu
                                    } else if (nameInput.isNotBlank() && emailInput.isNotBlank()) {
                                        launch {
                                            handleCreateProfile(profileService, nameInput, emailInput, state, ::updateState, ::setScreen, configurationManager)
                                        }
                                    }
                                }
                                "BACKSPACE" -> when (focusedField) {
                                    0 -> if (nameInput.isNotEmpty()) nameInput = nameInput.dropLast(1)
                                    1 -> if (emailInput.isNotEmpty()) emailInput = emailInput.dropLast(1)
                                }
                                else -> if (key.name.length == 1) {
                                    when (focusedField) {
                                        0 -> nameInput += key.name
                                        1 -> emailInput += key.name
                                    }
                                }
                            }
                        }
                        is ProfileScreen.Error, is ProfileScreen.Success, is ProfileScreen.Info -> {
                            when (key.name) {
                                "ENTER" -> {
                                    screen = ProfileScreen.MainMenu
                                    state = state.copy(
                                        errorMessage = null,
                                        successMessage = null,
                                        infoMessage = null
                                    )
                                }
                                else -> {
                                    screen = ProfileScreen.MainMenu
                                    state = state.copy(
                                        errorMessage = null,
                                        successMessage = null,
                                        infoMessage = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
} 