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

sealed class ConfigScreen {
    object MainMenu : ConfigScreen()

    object ViewAllConfig : ConfigScreen()

    object UpdateConfig : ConfigScreen()

    object Error : ConfigScreen()

    object Success : ConfigScreen()
}

data class ConfigState(
    val configList: ConfigListResponse? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null,
)

suspend fun configMode(configService: org.keyla.api.ConfigService) {
    coroutineScope {
        session {
            var screen by liveVarOf<ConfigScreen>(ConfigScreen.MainMenu)
            var state by liveVarOf(ConfigState())

            var configMenuIndex by liveVarOf(0)
            var updateConfigField by liveVarOf(0)

            var keyInput by liveVarOf("")
            var valueInput by liveVarOf("")

            fun updateState(newState: ConfigState) {
                state = newState
            }

            fun setScreen(newScreen: ConfigScreen) {
                screen = newScreen
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
                    is ConfigScreen.MainMenu -> {
                        renderHeader("BACKEND CONFIGURATION")
                        textLine("Manage backend server configuration")
                        textLine()
                        val options =
                            listOf(
                                "1. View All Configuration",
                                "2. Update Configuration Entry",
                                "3. Exit",
                            )
                        options.forEachIndexed { i, opt ->
                            if (i == configMenuIndex) {
                                cyan { textLine("> $opt") }
                            } else {
                                textLine("  $opt")
                            }
                        }
                        textLine()
                        yellow { textLine("Use arrow keys to navigate, Enter to select") }
                    }
                    is ConfigScreen.ViewAllConfig -> {
                        renderHeader("ALL CONFIGURATION ENTRIES")
                        state.configList?.entries?.forEach { entry ->
                            textLine("${entry.key.section}.${entry.key.key}")
                            textLine("  Value: ${entry.value}")
                            textLine("  Description: ${entry.description}")
                            textLine("  Type: ${entry.dataType}")
                            textLine("  Default: ${entry.defaultValue}")
                            textLine()
                        }
                        renderGoBackOption()
                    }
                    is ConfigScreen.UpdateConfig -> {
                        renderHeader("UPDATE CONFIGURATION ENTRY")
                        textLine("Key (dot notation): $keyInput")
                        textLine("Value: $valueInput")
                        textLine()
                        when (updateConfigField) {
                            0 -> yellow { textLine("Enter key in dot notation (e.g., server.port, database.useMongoDb): ") }
                            1 -> yellow { textLine("Enter new value: ") }
                            2 -> yellow { textLine("Go Back") }
                        }
                        textLine("Press Tab to switch fields, Enter to confirm")
                    }
                    is ConfigScreen.Error -> {
                        red { textLine("ERROR: ${state.errorMessage}") }
                        renderGoBackOption()
                    }
                    is ConfigScreen.Success -> {
                        green { textLine("SUCCESS: ${state.successMessage}") }
                        renderGoBackOption()
                    }
                }
            }.runUntilSignal {
                onKeyPressed {
                    when (screen) {
                        is ConfigScreen.MainMenu -> {
                            when (key.name) {
                                "UP" -> {
                                    configMenuIndex = (configMenuIndex - 1).coerceAtLeast(0)
                                }
                                "DOWN" -> {
                                    configMenuIndex = (configMenuIndex + 1).coerceAtMost(2)
                                }
                                "ENTER" -> {
                                    when (configMenuIndex) {
                                        0 -> launch { handleViewAllConfig(configService, state, ::updateState, ::setScreen) }
                                        1 -> screen = ConfigScreen.UpdateConfig
                                        2 -> signal()
                                    }
                                }
                            }
                        }
                        is ConfigScreen.ViewAllConfig -> {
                            when (key.name) {
                                "ENTER" -> screen = ConfigScreen.MainMenu
                                else -> screen = ConfigScreen.MainMenu
                            }
                        }
                        is ConfigScreen.UpdateConfig -> {
                            when (key.name) {
                                "TAB" -> updateConfigField = (updateConfigField + 1) % 3
                                "ENTER" -> {
                                    if (updateConfigField == 2) {
                                        screen = ConfigScreen.MainMenu
                                    } else if (keyInput.isNotBlank() && valueInput.isNotBlank()) {
                                        launch {
                                            handleUpdateConfig(configService, keyInput, valueInput, state, ::updateState, ::setScreen)
                                        }
                                    }
                                }
                                "BACKSPACE" ->
                                    when (updateConfigField) {
                                        0 -> if (keyInput.isNotEmpty()) keyInput = keyInput.dropLast(1)
                                        1 -> if (valueInput.isNotEmpty()) valueInput = valueInput.dropLast(1)
                                    }
                                else ->
                                    if (key.toString().length == 1) {
                                        when (updateConfigField) {
                                            0 -> keyInput += key.toString()
                                            1 -> valueInput += key.toString()
                                        }
                                    }
                            }
                        }
                        is ConfigScreen.Error, is ConfigScreen.Success -> {
                            when (key.name) {
                                "ENTER" -> {
                                    screen = ConfigScreen.MainMenu
                                    state =
                                        state.copy(
                                            errorMessage = null,
                                            successMessage = null,
                                        )
                                }
                                else -> {
                                    screen = ConfigScreen.MainMenu
                                    state =
                                        state.copy(
                                            errorMessage = null,
                                            successMessage = null,
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
