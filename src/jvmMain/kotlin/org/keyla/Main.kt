package org.keyla

import org.keyla.core.interfaces.ConfigurationStorage
import org.keyla.core.interfaces.PlatformService
import org.keyla.platform.JvmConfigurationStorage
import org.keyla.platform.JvmPlatformService

fun main(args: Array<String>) {
    System.setProperty("kotter.terminal.prefer.real", "true")
    
    val configurationStorage: ConfigurationStorage = getPlatformConfigurationStorage()
    val platformService: PlatformService = getPlatformService()
    
    val launcher = ApplicationLauncher(
        configurationStorage,
        platformService
    )
    
    launcher.launch(args)
}

actual fun getPlatformConfigurationStorage(): ConfigurationStorage {
    return JvmConfigurationStorage()
}

actual fun getPlatformService(): PlatformService {
    return JvmPlatformService()
} 