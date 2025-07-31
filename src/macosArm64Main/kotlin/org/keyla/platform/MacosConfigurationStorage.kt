package org.keyla.platform

import org.keyla.core.interfaces.ConfigurationStorage
import kotlinx.cinterop.*
import platform.posix.*

@OptIn(ExperimentalForeignApi::class)
class MacosConfigurationStorage : ConfigurationStorage {
    private val configDir = getConfigDirectory()
    private val configFile = "$configDir/keyla.conf"
    
    init {
        createConfigDirectory()
        if (!fileExists(configFile)) {
            saveString("api.base.url", "http://localhost:9999/api")
        }
    }
    
    override fun loadString(key: String): String? {
        return readConfigValue(key)
    }
    
    override fun saveString(key: String, value: String) {
        writeConfigValue(key, value)
    }
    
    override fun loadBoolean(key: String): Boolean? {
        val value = readConfigValue(key)
        return value?.toBoolean()
    }
    
    override fun saveBoolean(key: String, value: Boolean) {
        writeConfigValue(key, value.toString())
    }
    
    override fun loadInt(key: String): Int? {
        val value = readConfigValue(key)
        return value?.toIntOrNull()
    }
    
    override fun saveInt(key: String, value: Int) {
        writeConfigValue(key, value.toString())
    }
    
    private fun getConfigDirectory(): String {
        val homeDir = getenv("HOME")?.toKString() ?: "/tmp"
        return "$homeDir/Library/Application Support/keyla"
    }
    
    private fun createConfigDirectory() {
        memScoped {
            val mode = (S_IRWXU or S_IRWXG or S_IROTH or S_IXOTH).convert<mode_t>()
            mkdir(configDir, mode)
        }
    }
    
    private fun fileExists(path: String): Boolean {
        return memScoped {
            val stat = alloc<stat>()
            stat(path, stat.ptr) == 0
        }
    }
    
    private fun readConfigValue(key: String): String? {
        if (!fileExists(configFile)) return null
        
        return memScoped {
            val file = fopen(configFile, "r") ?: return@memScoped null
            try {
                val buffer = ByteArray(1024)
                val line = allocArray<ByteVar>(1024)
                
                while (fgets(line, 1024, file) != null) {
                    val lineStr = line.toKString()
                    if (lineStr.startsWith("$key=")) {
                        return@memScoped lineStr.substringAfter("=").trim()
                    }
                }
                null
            } finally {
                fclose(file)
            }
        }
    }
    
    private fun writeConfigValue(key: String, value: String) {
        memScoped {
            val tempFile = "$configFile.tmp"
            val file = fopen(tempFile, "w") ?: return@memScoped
            try {
                val existingValues = mutableMapOf<String, String>()
                
                if (fileExists(configFile)) {
                    val readFile = fopen(configFile, "r")
                    if (readFile != null) {
                        try {
                            val line = allocArray<ByteVar>(1024)
                            while (fgets(line, 1024, readFile) != null) {
                                val lineStr = line.toKString()
                                if (lineStr.contains("=")) {
                                    val parts = lineStr.split("=", limit = 2)
                                    if (parts.size == 2) {
                                        existingValues[parts[0].trim()] = parts[1].trim()
                                    }
                                }
                            }
                        } finally {
                            fclose(readFile)
                        }
                    }
                }
                
                existingValues[key] = value
                
                existingValues.forEach { (k, v) ->
                    fprintf(file, "%s=%s\n", k, v)
                }
            } finally {
                fclose(file)
            }
            
            rename(tempFile, configFile)
        }
    }
} 