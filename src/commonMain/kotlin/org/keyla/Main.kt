package org.keyla

import org.keyla.core.interfaces.ConfigurationStorage
import org.keyla.core.interfaces.PlatformService

/**
 * Creates a platform-specific configuration storage implementation.
 *
 * This function must be implemented by each platform-specific module to provide
 * the appropriate storage mechanism for that platform (e.g., file system, registry, etc.).
 *
 * @return A platform-specific ConfigurationStorage implementation
 */
expect fun getPlatformConfigurationStorage(): ConfigurationStorage

/**
 * Creates a platform-specific platform service implementation.
 *
 * This function must be implemented by each platform-specific module to provide
 * platform-specific functionality such as process management, time operations,
 * and user input handling.
 *
 * @return A platform-specific PlatformService implementation
 */
expect fun getPlatformService(): PlatformService
