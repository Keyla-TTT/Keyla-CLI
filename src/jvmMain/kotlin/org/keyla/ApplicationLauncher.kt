package org.keyla

import org.keyla.core.ApplicationContainer
import org.keyla.core.interfaces.ConfigurationStorage
import org.keyla.core.interfaces.PlatformService
import org.keyla.ui.AppMode

class ApplicationLauncher(
    private val configurationStorage: ConfigurationStorage,
    private val platformService: PlatformService
) {
    private val container = ApplicationContainer(
        configurationStorage,
        platformService
    )
    
    fun launch(args: Array<String>) {
        when {
            args.isEmpty() || args[0] == "start" || args[0] == "test" -> {
                launchMainApplication(AppMode.Test)
            }
            args[0] == "config" -> {
                launchMainApplication(AppMode.Config)
            }
            args[0] == "settings" -> {
                launchMainApplication(AppMode.Settings)
            }
            args[0] == "stats" -> {
                launchMainApplication(AppMode.Stats)
            }
            args[0] == "history" -> {
                launchMainApplication(AppMode.History)
            }
            args[0] == "profile" -> {
                launchMainApplication(AppMode.Profile)
            }
            else -> {
                showUsage()
            }
        }
    }
    
    private fun launchMainApplication(mode: AppMode) {
        val modeName = when (mode) {
            AppMode.Test -> "Typing Test"
            AppMode.Config -> "Configuration"
            AppMode.Settings -> "Settings"
            AppMode.Stats -> "Statistics"
            AppMode.History -> "Test History"
            AppMode.Profile -> "Profile Management"
        }
        
        try {
            val application = container.getApplication()
            application.run(mode)
        } catch (e: Exception) {
            println("Error: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun showUsage() {
        println("Keyla CLI - Typing Test Application")
        println()
        println("Usage:")
        println("  keyla                    Start the typing test application")
        println("  keyla test              Start the typing test application")
        println("  keyla config            Open configuration menu")
        println("  keyla settings          Open settings menu")
        println("  keyla stats             View statistics")
        println("  keyla history           View test history")
        println("  keyla profile           Manage profiles")
    }
} 