package org.keyla.api

import org.keyla.models.*

/**
 * Service interface for managing typing tests.
 *
 * This service handles all typing test operations including test creation, retrieval,
 * submission of results, and querying tests by various criteria. It provides the core
 * functionality for the typing test application's main features.
 */
interface TestService {
    /**
     * Creates a new typing test with the specified configuration.
     *
     * @param request The test creation request containing test parameters
     * @return A TestResponse containing the created test information
     */
    suspend fun createTest(request: TestRequest): TestResponse

    /**
     * Retrieves a specific test by its ID.
     *
     * @param testId The unique identifier of the test to retrieve
     * @return A TestResponse containing the test information
     */
    suspend fun getTest(testId: String): TestResponse

    /**
     * Retrieves all tests associated with a specific profile.
     *
     * @param profileId The ID of the profile to get tests for
     * @return A TestListResponse containing all tests for the profile
     */
    suspend fun getTestsForProfile(profileId: String): TestListResponse

    /**
     * Retrieves all tests for a specific language.
     *
     * @param language The language code to filter tests by
     * @return A TestListResponse containing all tests for the language
     */
    suspend fun getTestsByLanguage(language: String): TestListResponse

    /**
     * Submits test results for a completed typing test.
     *
     * @param testId The ID of the test to submit results for
     * @param results The test results data including timing and accuracy metrics
     * @return A TestResponse containing the updated test information
     */
    suspend fun submitTestResults(
        testId: String,
        results: TestResultsRequest,
    ): TestResponse

    /**
     * Retrieves the most recent test for a specific profile.
     *
     * @param profileId The ID of the profile to get the last test for
     * @return A LastTestResponse containing the most recent test information
     */
    suspend fun getLastTest(profileId: String): LastTestResponse
}
