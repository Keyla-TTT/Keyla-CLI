package org.keyla.core.interfaces

interface ConfigurationStorage {
    fun loadString(key: String): String?

    fun saveString(
        key: String,
        value: String,
    )

    fun loadBoolean(key: String): Boolean?

    fun saveBoolean(
        key: String,
        value: Boolean,
    )

    fun loadInt(key: String): Int?

    fun saveInt(
        key: String,
        value: Int,
    )
}
