package org.keyla.platform

import org.keyla.core.interfaces.PlatformService
import kotlin.system.exitProcess as kotlinExitProcess

/**
 * JVM-specific implementation of PlatformService using Java system utilities.
 * 
 * This implementation provides platform-specific operations for JVM applications,
 * including process management and time utilities.
 * 
 * @see org.keyla.core.interfaces.PlatformService
 */
class JvmPlatformService : PlatformService {
    override fun exitProcess(code: Int): Nothing {
        kotlinExitProcess(code)
    }
    
    override fun getCurrentTimeMillis(): Long {
        return System.currentTimeMillis()
    }
    
    override fun getUserInput(): String? {
        return readlnOrNull()
    }
} 