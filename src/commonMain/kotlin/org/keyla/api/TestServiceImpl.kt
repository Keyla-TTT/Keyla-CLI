package org.keyla.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.keyla.models.*

class TestServiceImpl(
    private val baseUrl: String,
    private val httpClient: HttpClient,
) : TestService {
    override suspend fun createTest(request: TestRequest): TestResponse {
        return httpClient.post("$baseUrl/tests") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun getTest(testId: String): TestResponse {
        return httpClient.get("$baseUrl/tests/$testId").body()
    }

    override suspend fun getTestsForProfile(profileId: String): TestListResponse {
        return httpClient.get("$baseUrl/profiles/$profileId/tests").body()
    }

    override suspend fun getTestsByLanguage(language: String): TestListResponse {
        return httpClient.get("$baseUrl/tests/language/$language").body()
    }

    override suspend fun submitTestResults(
        testId: String,
        results: TestResultsRequest,
    ): TestResponse {
        return httpClient.put("$baseUrl/tests/$testId/results") {
            contentType(ContentType.Application.Json)
            setBody(results)
        }.body()
    }

    override suspend fun getLastTest(profileId: String): LastTestResponse {
        return httpClient.get("$baseUrl/profiles/$profileId/last-test").body()
    }

    fun close() {
        httpClient.close()
    }
}
