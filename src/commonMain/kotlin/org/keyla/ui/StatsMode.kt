package org.keyla.ui

import kotlinx.coroutines.coroutineScope
import org.keyla.api.AnalyticsService
import org.keyla.models.AnalyticsResponse
import org.keyla.models.ProfileResponse
import org.keyla.util.format1f

suspend fun statsMode(
    analyticsService: AnalyticsService,
    currentProfile: ProfileResponse?,
) {
    coroutineScope {
        if (currentProfile == null) {
            println("No active profile found. Please select a profile first.")
            return@coroutineScope
        }

        try {
            println("Fetching analytics data...")
            val analytics = analyticsService.getUserAnalytics(currentProfile.id)
            displayAnalytics(analytics, currentProfile)
        } catch (e: Exception) {
            when {
                e.message?.contains("404") == true || e.message?.contains("Not Found") == true -> {
                    displayNoDataMessage(currentProfile)
                }
                else -> {
                    println("Failed to connect to the backend: ${getErrorMessage(e, "statsMode")}")
                }
            }
        }
    }
}

private fun displayNoDataMessage(profile: ProfileResponse) {
    println()
    println("╔══════════════════════════════════════════════════════════════╗")
    println("║                    TYPING STATISTICS                        ║")
    println("╚══════════════════════════════════════════════════════════════╝")
    println()

    println("👤 Profile: ${profile.name} (${profile.email})")
    println("📊 Total Tests Completed: 0")
    println()

    println("╔══════════════════════════════════════════════════════════════╗")
    println("║                    NO DATA AVAILABLE                        ║")
    println("╚══════════════════════════════════════════════════════════════╝")
    println()

    println("🎯 You haven't completed any typing tests yet!")
    println()
    println("To see your statistics, you need to:")
    println("   1. Start a typing test with 'keyla test'")
    println("   2. Complete the test")
    println("   3. Come back here to see your progress")
    println()

    println("╔══════════════════════════════════════════════════════════════╗")
    println("║                    QUICK START                              ║")
    println("╚══════════════════════════════════════════════════════════════╝")
    println()
    println("🚀 Ready to start? Run: keyla test")
    println()
}

private fun displayAnalytics(
    analytics: AnalyticsResponse,
    profile: ProfileResponse,
) {
    println()
    println("╔══════════════════════════════════════════════════════════════╗")
    println("║                    TYPING STATISTICS                        ║")
    println("╚══════════════════════════════════════════════════════════════╝")
    println()

    println("👤 Profile: ${profile.name} (${profile.email})")
    println("📊 Total Tests Completed: ${analytics.totalTests}")
    println()

    println("╔══════════════════════════════════════════════════════════════╗")
    println("║                    PERFORMANCE METRICS                      ║")
    println("╚══════════════════════════════════════════════════════════════╝")
    println()

    println("🏃 Speed (WPM):")
    println("   • Average: ${format1f(analytics.averageWpm)} WPM")
    println("   • Best: ${format1f(analytics.bestWpm)} WPM")
    println("   • Worst: ${format1f(analytics.worstWpm)} WPM")
    println("   • Improvement: ${format1f(analytics.wpmImprovement)} WPM")
    println()

    println("🎯 Accuracy:")
    println("   • Average: ${format1f(analytics.averageAccuracy)}%")
    println("   • Best: ${format1f(analytics.bestAccuracy)}%")
    println("   • Worst: ${format1f(analytics.worstAccuracy)}%")
    println("   • Improvement: ${format1f(analytics.accuracyImprovement)}%")
    println()

    println("❌ Errors:")
    println("   • Total Errors: ${analytics.totalErrors}")
    println("   • Average per Test: ${format1f(analytics.averageErrorsPerTest)}")
    println()

    if (analytics.totalTests > 0) {
        val errorRate = (analytics.totalErrors.toDouble() / analytics.totalTests) * 100
        println("📈 Error Rate: ${format1f(errorRate)}%")
        println()
    }

    println("╔══════════════════════════════════════════════════════════════╗")
    println("║                    SUMMARY                                  ║")
    println("╚══════════════════════════════════════════════════════════════╝")
    println()

    when {
        analytics.totalTests == 0 -> {
            println("🎉 Welcome! Complete your first typing test to see your statistics.")
        }
        analytics.wpmImprovement > 0 && analytics.accuracyImprovement > 0 -> {
            println("🚀 Excellent progress! You're improving in both speed and accuracy.")
        }
        analytics.wpmImprovement > 0 -> {
            println("⚡ Great job! Your typing speed is improving.")
        }
        analytics.accuracyImprovement > 0 -> {
            println("🎯 Well done! Your accuracy is getting better.")
        }
        else -> {
            println("💪 Keep practicing! Consistency is key to improvement.")
        }
    }

    println()
}
