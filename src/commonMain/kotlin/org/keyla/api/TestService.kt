package org.keyla.api

import org.keyla.models.*

interface TestService {
    suspend fun createTest(request: TestRequest): TestResponse
    suspend fun getTest(testId: String): TestResponse
    suspend fun getTestsForProfile(profileId: String): TestListResponse
    suspend fun getTestsByLanguage(language: String): TestListResponse
    suspend fun submitTestResults(testId: String, results: TestResultsRequest): TestResponse
    suspend fun getLastTest(profileId: String): LastTestResponse
} 