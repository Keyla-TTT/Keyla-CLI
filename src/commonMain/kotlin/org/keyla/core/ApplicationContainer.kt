package org.keyla.core

import org.keyla.api.ApiServiceFactory
import org.keyla.api.ApiServiceFactoryImpl
import org.keyla.core.interfaces.*

class ApplicationContainer(
    configurationStorage: ConfigurationStorage,
    platformService: PlatformService,
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
