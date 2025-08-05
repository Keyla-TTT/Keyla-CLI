package org.keyla.util

import kotlin.system.exitProcess

actual fun exitProcess(code: Int): Nothing {
    exitProcess(code)
}

actual fun getCurrentTimeMillis(): Long {
    return System.currentTimeMillis()
}
