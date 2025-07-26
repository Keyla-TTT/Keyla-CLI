package org.keyla.core

import org.keyla.api.*
import org.keyla.core.interfaces.*
import org.keyla.models.*
import kotlin.test.*

class TypingTestApplicationTest {
    
    private lateinit var mockApiServiceFactory: MockApiServiceFactory
    private lateinit var mockPlatformService: MockPlatformService
    private lateinit var mockConfigurationManager: MockConfigurationManager
    private lateinit var application: TypingTestApplication
    
    @BeforeTest
    fun setup() {
        mockApiServiceFactory = MockApiServiceFactory()
        mockPlatformService = MockPlatformService()
        mockConfigurationManager = MockConfigurationManager()
        
        application = TypingTestApplication(
            mockApiServiceFactory,
            mockPlatformService,
            mockConfigurationManager
        )
    }
    
    @Test
    fun `should be initialized with correct dependencies`() {
        assertNotNull(application)
    }
}

class MockApiServiceFactory : ApiServiceFactory {
    val testService = MockTestService()
    val profileService = MockProfileService()
    val configService = MockConfigService()
    val languageService = MockLanguageService()
    val analyticsService = MockAnalyticsService()
    val statisticsService = MockStatisticsService()
    
    var closeCalled = false
    
    override fun createTestService(): TestService = testService
    override fun createProfileService(): ProfileService = profileService
    override fun createConfigService(): ConfigService = configService
    override fun createLanguageService(): LanguageService = languageService
    override fun createAnalyticsService(): AnalyticsService = analyticsService
    override fun createStatisticsService(): StatisticsService = statisticsService
    override fun close() { closeCalled = true }
}

class MockTestService : TestService {
    override suspend fun createTest(request: TestRequest): TestResponse = 
        TestResponse("test123", request.profileId, createdAt = "2024-01-01T00:00:00Z")
    override suspend fun getTest(testId: String): TestResponse = 
        TestResponse(testId, "profile123", createdAt = "2024-01-01T00:00:00Z")
    override suspend fun getTestsForProfile(profileId: String): TestListResponse = TestListResponse(emptyList())
    override suspend fun getTestsByLanguage(language: String): TestListResponse = TestListResponse(emptyList())
    override suspend fun submitTestResults(testId: String, results: TestResultsRequest): TestResponse = 
        TestResponse(testId, "profile123", createdAt = "2024-01-01T00:00:00Z")
    override suspend fun getLastTest(profileId: String): LastTestResponse = LastTestResponse()
}

class MockProfileService : ProfileService {
    var getAllProfilesCalled = false
    var profiles = listOf<ProfileResponse>()
    
    override suspend fun getAllProfiles(): ProfileListResponse {
        getAllProfilesCalled = true
        return ProfileListResponse(profiles)
    }
    
    override suspend fun createProfile(request: CreateProfileRequest): ProfileResponse = ProfileResponse("new-profile", request.name, request.email)
}

class MockConfigService : ConfigService {
    var testConnectionCalled = false
    var connectionTestResult = true
    
    override suspend fun getAllConfig(): ConfigListResponse = ConfigListResponse(emptyList())
    override suspend fun getCurrentConfig(): AppConfig = AppConfig(DatabaseConfig("", "", false), ServerConfig("", 0, ThreadPoolConfig(0, 0, 0, 0, "")), DictionaryConfig("", false), "")
    override suspend fun getConfigEntry(section: String, key: String): ConfigEntry = ConfigEntry(ConfigKey(section, key), "", "", "", "")
    override suspend fun updateConfig(request: SimpleConfigUpdateRequest): ConfigUpdateResponse = ConfigUpdateResponse(true, "")
    override suspend fun reloadConfig(): AppConfig = AppConfig(DatabaseConfig("", "", false), ServerConfig("", 0, ThreadPoolConfig(0, 0, 0, 0, "")), DictionaryConfig("", false), "")
    override suspend fun resetConfig(): AppConfig = AppConfig(DatabaseConfig("", "", false), ServerConfig("", 0, ThreadPoolConfig(0, 0, 0, 0, "")), DictionaryConfig("", false), "")
    override suspend fun testConnection(): Boolean {
        testConnectionCalled = true
        return connectionTestResult
    }
}

class MockLanguageService : LanguageService {
    override suspend fun getDictionaries(): DictionariesResponse = DictionariesResponse(emptyList())
    override suspend fun getMergers(): MergersResponse = MergersResponse(emptyList())
    override suspend fun getModifiers(): ModifiersResponse = ModifiersResponse(emptyList())
}

class MockAnalyticsService : AnalyticsService {
    override suspend fun getUserAnalytics(userId: String): AnalyticsResponse = AnalyticsResponse(userId, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0.0)
}

class MockStatisticsService : StatisticsService {
    override suspend fun getProfileStatistics(profileId: String): ProfileStatisticsListResponse = ProfileStatisticsListResponse(profileId, emptyList())
}

class MockConfigurationManager : ConfigurationManager {
    private var _activeProfileId: String? = null
    
    override fun getApiBaseUrl(): String = "http://localhost:8080/api"
    override fun setApiBaseUrl(url: String) {}
    override fun getActiveProfileId(): String? = _activeProfileId
    override fun setActiveProfileId(profileId: String) { _activeProfileId = profileId }
    override fun loadConfiguration() {}
    override fun saveConfiguration() {}
} 