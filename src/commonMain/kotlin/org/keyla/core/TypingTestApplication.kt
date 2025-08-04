package org.keyla.core

import kotlinx.coroutines.runBlocking
import org.keyla.api.*
import org.keyla.core.interfaces.*
import org.keyla.models.ProfileResponse
import org.keyla.ui.AppMode
import org.keyla.ui.configMode
import org.keyla.ui.historyMode
import org.keyla.ui.profileMode
import org.keyla.ui.settingsMode
import org.keyla.ui.statsMode
import org.keyla.ui.testMode

class TypingTestApplication(
    private val apiServiceFactory: ApiServiceFactory,
    private val platformService: PlatformService,
    private val configurationManager: ConfigurationManager,
) {
    private var currentProfile: ProfileResponse? = null

    private val testService = apiServiceFactory.createTestService()
    private val profileService = apiServiceFactory.createProfileService()
    private val configService = apiServiceFactory.createConfigService()
    private val languageService = apiServiceFactory.createLanguageService()
    private val analyticsService = apiServiceFactory.createAnalyticsService()
    private val statisticsService = apiServiceFactory.createStatisticsService()

    fun run(mode: AppMode = AppMode.Test) {
        runBlocking {
            try {
                when (mode) {
                    AppMode.Settings -> {
                        settingsMode(configService, profileService, configurationManager)
                    }
                    AppMode.Profile -> {
                        if (!establishConnection()) {
                            return@runBlocking
                        }
                        profileMode(profileService, currentProfile, configurationManager)
                    }
                    else -> {
                        if (!establishConnection()) {
                            return@runBlocking
                        }

                        if (mode == AppMode.Test || mode == AppMode.History || mode == AppMode.Stats) {
                            loadInitialData()
                        }

                        when (mode) {
                            AppMode.Test -> testMode(testService, languageService, currentProfile, emptyList())
                            AppMode.Config -> configMode(configService)
                            AppMode.History -> historyMode(testService, statisticsService, currentProfile)
                            AppMode.Stats -> statsMode(analyticsService, currentProfile)
                            else -> {}
                        }
                    }
                }
            } catch (e: Exception) {
                println("Application error: ${e.message}")
            } finally {
                apiServiceFactory.close()
            }
        }
    }

    private suspend fun establishConnection(): Boolean {
        return try {
            configService.testConnection()
        } catch (e: Exception) {
            println("Application error: ${e.message}")
            println("connection failed, try again or change the backend url running 'keyla settings'")
            platformService.exitProcess(1)
        }
    }

    private suspend fun loadInitialData() {
        try {
            try {
                val profilesResponse = profileService.getAllProfiles()
                if (profilesResponse.profiles.isNotEmpty()) {
                    val activeProfileId = configurationManager.getActiveProfileId()
                    currentProfile =
                        if (activeProfileId != null) {
                            profilesResponse.profiles.find { it.id == activeProfileId }
                        } else {
                            profilesResponse.profiles.first()
                        }

                    if (currentProfile == null) {
                        currentProfile = profilesResponse.profiles.first()
                        currentProfile?.let { configurationManager.setActiveProfileId(it.id) }
                    }
                } else {
                    println("No profiles found. Please create a profile first using 'keyla profile'")
                    platformService.exitProcess(1)
                }
            } catch (e: Exception) {
                println("No profiles found. Please create a profile first using 'keyla profile'")
                platformService.exitProcess(1)
            }
        } catch (e: Exception) {
            println("Failed to load initial data: ${e.message}")
            platformService.exitProcess(1)
        }
    }
}
