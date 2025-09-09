package org.keyla.api

import io.ktor.client.*

/**
 * Creates a platform-specific HTTP client instance.
 *
 * This function must be implemented by each platform-specific module to provide
 * the appropriate HTTP client configuration for that platform, including any
 * platform-specific networking settings, SSL configurations, or engine-specific
 * optimizations.
 *
 * @return A platform-specific HttpClient instance configured for the target platform
 */
expect fun createHttpClient(): HttpClient
