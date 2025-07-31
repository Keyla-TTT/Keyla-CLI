package org.keyla.util

import kotlinx.cinterop.*
import platform.posix.*

@OptIn(ExperimentalForeignApi::class)
actual fun exitProcess(code: Int): Nothing {
    exit(code)
    throw IllegalStateException("Should never reach here")
}

@OptIn(ExperimentalForeignApi::class)
actual fun getCurrentTimeMillis(): Long {
    return platform.posix.time(null) * 1000
} 