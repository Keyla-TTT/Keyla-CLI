package org.keyla.platform

import org.keyla.core.interfaces.ConfigurationStorage
import kotlinx.cinterop.*
import platform.posix.*

@OptIn(ExperimentalForeignApi::class)
class MingwConfigurationStorage : ConfigurationStorage {
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
        return loadString(key)?.toBooleanStrictOrNull()
    }

    override fun saveBoolean(key: String, value: Boolean) {
        saveString(key, value.toString())
    }

    override fun loadInt(key: String): Int? {
        return loadString(key)?.toIntOrNull()
    }

    override fun saveInt(key: String, value: Int) {
        saveString(key, value.toString())
    }

    private fun getConfigDirectory(): String {
        val appData = getenv("APPDATA")?.toKString() ?: error("APPDATA not set")
        val dir = "$appData/keyla"
        return dir
    }

    private fun createConfigDirectory() {
        mkdir(configDir)
    }

    private fun fileExists(path: String): Boolean {
        return access(path, F_OK) == 0
    }

    private fun readConfigValue(key: String): String? {
        val file = fopen(configFile, "r") ?: return null
        try {
            memScoped {
                val buffer = ByteArray(1024)
                while (fgets(buffer.refTo(0), buffer.size, file) != null) {
                    val line = buffer.toKString().trim()
                    if (line.startsWith("$key=")) {
                        return line.substringAfter("=")
                    }
                }
            }
        } finally {
            fclose(file)
        }
        return null
    }

    private fun writeConfigValue(key: String, value: String) {
        val map = mutableMapOf<String, String>()
        val file = fopen(configFile, "r")
        if (file != null) {
            try {
                memScoped {
                    val buffer = ByteArray(1024)
                    while (fgets(buffer.refTo(0), buffer.size, file) != null) {
                        val line = buffer.toKString().trim()
                        if (line.contains("=")) {
                            val (k, v) = line.split("=", limit = 2)
                            map[k] = v
                        }
                    }
                }
            } finally {
                fclose(file)
            }
        }
        map[key] = value
        val out = fopen(configFile, "w") ?: return
        try {
            for ((k, v) in map) {
                fputs("$k=$v\n", out)
            }
        } finally {
            fclose(out)
        }
    }
} 