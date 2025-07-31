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
        
        println("Keyla CLI - $modeName")
        println("Starting $modeName application...")
        
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
        println("Usage: keyla [command]")
        println()
        println("Commands:")
        println("  start, test    Start typing test mode")
        println("  config         Open configuration mode")
        println("  settings       Open settings mode")
        println("  stats          View statistics")
        println("  history        View test history")
        println("  profile        Manage user profiles")
        println()
        println("Examples:")
        println("  keyla          Start typing test")
        println("  keyla config   Open configuration")
        println("  keyla settings Open settings")
    }
} 