package org.keyla.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import org.keyla.core.interfaces.PlatformService
import platform.posix.exit
import platform.windows.FILETIME
import platform.windows.GetSystemTimeAsFileTime

/**
 * Windows-specific implementation of PlatformService for native Windows applications.
 *
 * This implementation provides platform-specific operations for Windows applications,
 * including process management and time utilities.
 *
 * @see org.keyla.core.interfaces.PlatformService
 */
@OptIn(ExperimentalForeignApi::class)
class MingwPlatformService : PlatformService {
    override fun exitProcess(code: Int): Nothing {
        exit(code)
        throw IllegalStateException("Should never reach here")
    }

    override fun getCurrentTimeMillis(): Long {
        return memScoped {
            val fileTime = alloc<FILETIME>()
            GetSystemTimeAsFileTime(fileTime.ptr)
            val windowsTime = (fileTime.dwHighDateTime.toLong() shl 32) or fileTime.dwLowDateTime.toLong()
            val unixTime = (windowsTime - 116444736000000000L) / 10000L
            unixTime
        }
    }

    override fun getUserInput(): String? {
        return readlnOrNull()
    }
} 
