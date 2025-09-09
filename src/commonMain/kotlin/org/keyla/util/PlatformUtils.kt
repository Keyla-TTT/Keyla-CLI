package org.keyla.util

/**
 * Terminates the current process with the specified exit code.
 *
 * This function must be implemented by each platform-specific module to provide
 * the appropriate process termination mechanism for that platform.
 *
 * @param code The exit code to return to the operating system
 * @return This function never returns normally
 */
expect fun exitProcess(code: Int): Nothing

/**
 * Gets the current time in milliseconds since the Unix epoch.
 *
 * This function must be implemented by each platform-specific module to provide
 * the appropriate time retrieval mechanism for that platform.
 *
 * @return The current time in milliseconds since January 1, 1970, 00:00:00 GMT
 */
expect fun getCurrentTimeMillis(): Long
