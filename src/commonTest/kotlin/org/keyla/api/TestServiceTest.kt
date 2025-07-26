package org.keyla.api

import org.keyla.models.*
import kotlin.test.*

class TestServiceTest {
    
    @Test
    fun `TestRequest should have correct structure`() {
        val request = TestRequest(
            profileId = "profile123",
            sources = listOf(SourceWithMerger("english", "standard")),
            wordCount = 50,
            modifiers = listOf("punctuation", "numbers"),
            timeLimit = 300000L
        )
        
        assertEquals("profile123", request.profileId)
        assertEquals(1, request.sources.size)
        assertEquals("english", request.sources[0].name)
        assertEquals("standard", request.sources[0].merger)
        assertEquals(50, request.wordCount)
        assertEquals(listOf("punctuation", "numbers"), request.modifiers)
        assertEquals(300000L, request.timeLimit)
    }
    
    @Test
    fun `TestResponse should have correct structure`() {
        val response = TestResponse(
            testId = "test123",
            profileId = "profile123",
            words = listOf("hello", "world"),
            sources = listOf(SourceWithMerger("english", "standard")),
            modifiers = listOf("punctuation"),
            createdAt = "2024-01-01T00:00:00Z",
            completedAt = "2024-01-01T00:05:00Z",
            accuracy = 95.5,
            rawAccuracy = 97.0,
            testTime = 300000L,
            errorCount = 2,
            errorWordIndices = listOf(5, 12),
            timeLimit = 300000L
        )
        
        assertEquals("test123", response.testId)
        assertEquals("profile123", response.profileId)
        assertEquals(listOf("hello", "world"), response.words)
        assertEquals(95.5, response.accuracy)
        assertEquals(97.0, response.rawAccuracy)
        assertEquals(300000L, response.testTime)
        assertEquals(2, response.errorCount)
        assertEquals(listOf(5, 12), response.errorWordIndices)
        assertEquals(300000L, response.timeLimit)
    }
    
    @Test
    fun `TestResultsRequest should have correct structure`() {
        val results = TestResultsRequest(
            accuracy = 95.5,
            rawAccuracy = 97.0,
            testTime = 300000L,
            errorCount = 2,
            errorWordIndices = listOf(5, 12)
        )
        
        assertEquals(95.5, results.accuracy)
        assertEquals(97.0, results.rawAccuracy)
        assertEquals(300000L, results.testTime)
        assertEquals(2, results.errorCount)
        assertEquals(listOf(5, 12), results.errorWordIndices)
    }
    
    @Test
    fun `LastTestResponse should have correct structure`() {
        val response = LastTestResponse(
            words = listOf("last", "test", "words"),
            timeLimit = 300000L
        )
        
        assertEquals(listOf("last", "test", "words"), response.words)
        assertEquals(300000L, response.timeLimit)
    }
    
    @Test
    fun `TestListResponse should have correct structure`() {
        val response = TestListResponse(
            tests = listOf(
                TestResponse(
                    testId = "test1",
                    profileId = "profile123",
                    words = listOf("test", "words"),
                    createdAt = "2024-01-01T00:00:00Z"
                ),
                TestResponse(
                    testId = "test2",
                    profileId = "profile123",
                    words = listOf("more", "words"),
                    createdAt = "2024-01-02T00:00:00Z"
                )
            )
        )
        
        assertEquals(2, response.tests.size)
        assertEquals("test1", response.tests[0].testId)
        assertEquals("test2", response.tests[1].testId)
    }
} 