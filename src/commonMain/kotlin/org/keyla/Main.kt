package org.keyla

import org.keyla.core.interfaces.ConfigurationStorage
import org.keyla.core.interfaces.PlatformService

expect fun getPlatformConfigurationStorage(): ConfigurationStorage
expect fun getPlatformService(): PlatformService 