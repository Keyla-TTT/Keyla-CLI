package org.keyla.core.interfaces

interface PlatformService {
    fun exitProcess(code: Int): Nothing
    fun getCurrentTimeMillis(): Long
    fun getUserInput(): String?
} 