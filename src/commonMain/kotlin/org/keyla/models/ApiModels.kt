package org.keyla.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateProfileRequest(
    val name: String,
    val email: String,
    val settings: Set<String> = emptySet()
)

@Serializable
data class ProfileResponse(
    val id: String,
    val name: String,
    val email: String,
    val settings: Set<String> = emptySet()
)

@Serializable
data class ProfileListResponse(
    val profiles: List<ProfileResponse>
)

@Serializable
data class SourceWithMerger(
    val name: String,
    val merger: String? = null
)

@Serializable
data class TestRequest(
    val profileId: String,
    val sources: List<SourceWithMerger> = emptyList(),
    val wordCount: Int,
    val modifiers: List<String> = emptyList(),
    val timeLimit: Long? = null
)

@Serializable
data class TestResponse(
    val testId: String,
    val profileId: String,
    val words: List<String> = emptyList(),
    val sources: List<SourceWithMerger> = emptyList(),
    val modifiers: List<String> = emptyList(),
    val createdAt: String,
    val completedAt: String? = null,
    val accuracy: Double? = null,
    val rawAccuracy: Double? = null,
    val testTime: Long? = null,
    val errorCount: Int? = null,
    val errorWordIndices: List<Int> = emptyList(),
    val timeLimit: Long? = null
)

@Serializable
data class TestListResponse(
    val tests: List<TestResponse>
)

@Serializable
data class TestResultsRequest(
    val accuracy: Double,
    val rawAccuracy: Double,
    val testTime: Long,
    val errorCount: Int,
    val errorWordIndices: List<Int> = emptyList()
)

@Serializable
data class LastTestResponse(
    val words: List<String> = emptyList(),
    val timeLimit: Long? = null
)

@Serializable
data class DictionaryInfo(
    val name: String
)

@Serializable
data class DictionariesResponse(
    val dictionaries: List<DictionaryInfo>
)

@Serializable
data class MergerInfo(
    val name: String,
    val description: String
)

@Serializable
data class MergersResponse(
    val mergers: List<MergerInfo>
)

@Serializable
data class ModifierInfo(
    val name: String,
    val description: String
)

@Serializable
data class ModifiersResponse(
    val modifiers: List<ModifierInfo>
)

@Serializable
data class LanguageInfo(
    val language: String,
    val dictionaries: List<String> = emptyList()
)

@Serializable
data class LanguagesResponse(
    val languages: List<LanguageInfo>
)

@Serializable
data class ErrorResponse(
    val message: String,
    val code: String,
    val statusCode: Int
)

@Serializable
data class ConfigKey(
    val section: String,
    val key: String
)

@Serializable
data class ConfigEntry(
    val key: ConfigKey,
    val value: String,
    val description: String,
    val dataType: String,
    val defaultValue: String
)

@Serializable
data class ConfigListResponse(
    val entries: List<ConfigEntry>
)

@Serializable
data class SimpleConfigUpdateRequest(
    val key: String,
    val value: String
)

@Serializable
data class ConfigUpdateResponse(
    val success: Boolean,
    val message: String
)

@Serializable
data class AppConfig(
    val database: DatabaseConfig,
    val server: ServerConfig,
    val dictionary: DictionaryConfig,
    val version: String
)

@Serializable
data class DatabaseConfig(
    val mongoUri: String,
    val databaseName: String,
    val useMongoDb: Boolean
)

@Serializable
data class ServerConfig(
    val host: String,
    val port: Int,
    val threadPool: ThreadPoolConfig
)

@Serializable
data class ThreadPoolConfig(
    val coreSize: Int,
    val maxSize: Int,
    val keepAliveSeconds: Int,
    val queueSize: Int,
    val threadNamePrefix: String
)

@Serializable
data class DictionaryConfig(
    val basePath: String,
    val autoCreateDirectories: Boolean
)

@Serializable
data class AnalyticsResponse(
    val userId: String,
    val totalTests: Int,
    val averageWpm: Double,
    val averageAccuracy: Double,
    val bestWpm: Double,
    val worstWpm: Double,
    val bestAccuracy: Double,
    val worstAccuracy: Double,
    val wpmImprovement: Double,
    val accuracyImprovement: Double,
    val totalErrors: Int,
    val averageErrorsPerTest: Double
)

@Serializable
data class StatisticsResponse(
    val testId: String,
    val profileId: String,
    val wpm: Double,
    val accuracy: Double,
    val errors: List<Int> = emptyList(),
    val timestamp: Long
)

@Serializable
data class ProfileStatisticsListResponse(
    val profileId: String,
    val statistics: List<StatisticsResponse>
) 