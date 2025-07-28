package org.keyla.ui

import com.varabyte.kotter.foundation.input.onKeyPressed
import com.varabyte.kotter.foundation.liveVarOf
import com.varabyte.kotter.foundation.runUntilSignal
import com.varabyte.kotter.foundation.session
import com.varabyte.kotter.foundation.text.*
import org.keyla.models.*
import org.keyla.util.format1f
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

sealed class HistoryScreen {
    object TestHistory : HistoryScreen()
    object Error : HistoryScreen()
}

data class HistoryState(
    val currentProfile: ProfileResponse? = null,
    val testHistory: List<TestResponse> = emptyList(),
    val statistics: List<StatisticsResponse> = emptyList(),
    val errorMessage: String? = null
)

suspend fun historyMode(
    testService: org.keyla.api.TestService,
    statisticsService: org.keyla.api.StatisticsService,
    initialProfile: ProfileResponse?
) {
    coroutineScope {
        session {
            var screen by liveVarOf<HistoryScreen>(HistoryScreen.TestHistory)
            var state by liveVarOf(HistoryState(currentProfile = initialProfile))
            
            fun updateState(newState: HistoryState) { state = newState }
            fun setScreen(newScreen: HistoryScreen) { screen = newScreen }

            if (initialProfile != null) {
                launch {
                    try {
                        val testsResponse = testService.getTestsForProfile(initialProfile.id)
                        val statisticsResponse = statisticsService.getProfileStatistics(initialProfile.id)
                        updateState(state.copy(
                            testHistory = testsResponse.tests,
                            statistics = statisticsResponse.statistics
                        ))
                    } catch (e: Exception) {
                        updateState(state.copy(
                            testHistory = emptyList(),
                            statistics = emptyList(),
                            errorMessage = "Failed to load data: ${e.message}"
                        ))
                    }
                }
            }

            section {
                fun renderHeader(title: String) {
                    green { textLine("╔══════════════════════════════════════════════════════════════╗") }
                    green { textLine("║                          $title                        ║") }
                    green { textLine("╚══════════════════════════════════════════════════════════════╝") }
                    textLine()
                }
                
                when (screen) {
                    is HistoryScreen.TestHistory -> {
                        renderHeader("TEST HISTORY")
                        val currentProfile = state.currentProfile
                        if (currentProfile == null) {
                            red { textLine("No profile selected. Please run 'keyla test' first to create a profile.") }
                        } else if (state.testHistory.isEmpty()) {
                            yellow { textLine("No tests found for profile: ${currentProfile.name}") }
                        } else {
                            textLine("Profile: ${currentProfile.name}")
                            textLine()
                            
                            val statisticsMap = state.statistics.associateBy { it.testId }
                            
                            state.testHistory.takeLast(10).forEach { test ->
                                val status = if (test.completedAt != null) "Completed" else "Incomplete"
                                val testStats = statisticsMap[test.testId]
                                val wpm = testStats?.wpm?.let { "${format1f(it)} WPM" } ?: "N/A"
                                val accuracy = testStats?.accuracy?.let { "${format1f(it)}%" } ?: "N/A"
                                val sourcesText = test.sources.joinToString(", ") { it.name }
                                val modifiersText = if (test.modifiers.isNotEmpty()) " + ${test.modifiers.joinToString(", ")}" else ""
                                
                                textLine("$sourcesText$modifiersText - $status")
                                textLine("  WPM: $wpm | Accuracy: $accuracy")
                                textLine()
                            }
                        }
                        textLine()
                        yellow { textLine("Press any key to exit...") }
                    }
                    is HistoryScreen.Error -> {
                        red { textLine("ERROR: ${state.errorMessage}") }
                        textLine()
                        yellow { textLine("Press any key to exit...") }
                    }
                }
            }.runUntilSignal {
                onKeyPressed {
                    signal()
                }
            }
        }
    }
} 