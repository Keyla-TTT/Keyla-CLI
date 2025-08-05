package org.keyla

import org.keyla.core.interfaces.ConfigurationStorage
import org.keyla.core.interfaces.PlatformService
import org.keyla.platform.MingwConfigurationStorage
import org.keyla.platform.MingwPlatformService

fun main(args: Array<String>) {
    try {
        val configurationStorage: ConfigurationStorage = getPlatformConfigurationStorage()
        val platformService: PlatformService = getPlatformService()

        val launcher =
            ApplicationLauncher(
                configurationStorage,
                platformService,
            )

        launcher.launch(args)
    } catch (e: Exception) {
        val errorMessage = e.message ?: "Unknown error occurred"
        println("Fatal error: $errorMessage")
        e.printStackTrace()
        kotlin.system.exitProcess(1)
    }
}

actual fun getPlatformConfigurationStorage(): ConfigurationStorage {
    return MingwConfigurationStorage()
}

actual fun getPlatformService(): PlatformService {
    return MingwPlatformService()
}
