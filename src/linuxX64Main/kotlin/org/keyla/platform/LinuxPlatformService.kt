
package org.keyla.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import org.keyla.core.interfaces.PlatformService
import platform.posix.CLOCK_REALTIME
import platform.posix.clock_gettime
import platform.posix.exit
import platform.posix.timespec

@OptIn(ExperimentalForeignApi::class)
class LinuxPlatformService : PlatformService {
    override fun exitProcess(code: Int): Nothing {
        exit(code)
        throw IllegalStateException("Should never reach here")
    }

    override fun getCurrentTimeMillis(): Long {
        return memScoped {
            val timespec = alloc<timespec>()
            clock_gettime(CLOCK_REALTIME, timespec.ptr)
            timespec.tv_sec * 1000L + timespec.tv_nsec / 1_000_000L
        }
    }

    override fun getUserInput(): String? {
        return readlnOrNull()
    }
} 
