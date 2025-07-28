package org.keyla.core

import org.keyla.core.interfaces.*
import org.keyla.api.ApiServiceFactory
import org.keyla.api.ApiServiceFactoryImpl

class ApplicationContainer(
    configurationStorage: ConfigurationStorage,
    platformService: PlatformService
) {
    private val configurationManager = AppConfigurationManager(configurationStorage)
    private val apiServiceFactory = createApiServiceFactory(configurationManager.getApiBaseUrl())
    private val application = TypingTestApplication(apiServiceFactory, platformService, configurationManager)
    
    fun getApplication(): TypingTestApplication = application
    fun getConfigurationManager(): ConfigurationManager = configurationManager
    fun getApiServiceFactory(): ApiServiceFactory = apiServiceFactory
    
    private fun createApiServiceFactory(baseUrl: String): ApiServiceFactory {
        return createPlatformApiServiceFactory(baseUrl)
    }
}

fun createPlatformApiServiceFactory(baseUrl: String): ApiServiceFactory {
    return ApiServiceFactoryImpl(baseUrl)
}