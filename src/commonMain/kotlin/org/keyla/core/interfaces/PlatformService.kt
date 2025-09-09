package org.keyla.core.interfaces

/**
 * Interface for platform-specific operations.
 *
 * This interface abstracts platform-specific functionality that varies between different
 * operating systems and environments. It provides methods for process management, time
 * operations, and user input handling that are implemented differently for each platform.
 */
interface PlatformService {
    /**
     * Terminates the current process with the specified exit code.
     *
     * @param code The exit code to return to the operating system
     * @return This function never returns normally
     */
    fun exitProcess(code: Int): Nothing

    /**
     * Gets the current time in milliseconds since the Unix epoch.
     *
     * @return The current time in milliseconds
     */
    fun getCurrentTimeMillis(): Long

    /**
     * Reads user input from the standard input stream.
     *
     * @return The user input as a string, or null if no input is available
     */
    fun getUserInput(): String?
}
