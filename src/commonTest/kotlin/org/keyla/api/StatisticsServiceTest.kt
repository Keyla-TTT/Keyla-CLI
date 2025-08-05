package org.keyla.api

import org.keyla.models.*
import kotlin.test.*

class StatisticsServiceTest {
    @Test
    fun `ProfileStatisticsListResponse should have correct structure`() {
        val response =
            ProfileStatisticsListResponse(
                profileId = "profile123",
                statistics =
                    listOf(
                        StatisticsResponse(
                            testId = "test1",
                            profileId = "profile123",
                            wpm = 65.5,
                            accuracy = 92.3,
                            errors = listOf(5, 12, 18),
                            timestamp = 1640995200000L,
                        ),
                        StatisticsResponse(
                            testId = "test2",
                            profileId = "profile123",
                            wpm = 72.1,
                            accuracy = 95.7,
                            errors = listOf(2, 8),
                            timestamp = 1641081600000L,
                        ),
                        StatisticsResponse(
                            testId = "test3",
                            profileId = "profile123",
                            wpm = 68.9,
                            accuracy = 89.2,
                            errors = listOf(3, 7, 11, 15),
                            timestamp = 1641168000000L,
                        ),
                    ),
            )

        assertEquals("profile123", response.profileId)
        assertEquals(3, response.statistics.size)

        assertEquals("test1", response.statistics[0].testId)
        assertEquals(65.5, response.statistics[0].wpm, 0.01)
        assertEquals(92.3, response.statistics[0].accuracy, 0.01)
        assertEquals(listOf(5, 12, 18), response.statistics[0].errors)
        assertEquals(1640995200000L, response.statistics[0].timestamp)

        assertEquals("test2", response.statistics[1].testId)
        assertEquals(72.1, response.statistics[1].wpm, 0.01)
        assertEquals(95.7, response.statistics[1].accuracy, 0.01)
        assertEquals(listOf(2, 8), response.statistics[1].errors)
        assertEquals(1641081600000L, response.statistics[1].timestamp)

        assertEquals("test3", response.statistics[2].testId)
        assertEquals(68.9, response.statistics[2].wpm, 0.01)
        assertEquals(89.2, response.statistics[2].accuracy, 0.01)
        assertEquals(listOf(3, 7, 11, 15), response.statistics[2].errors)
        assertEquals(1641168000000L, response.statistics[2].timestamp)
    }

    @Test
    fun `StatisticsResponse should have correct structure`() {
        val stats =
            StatisticsResponse(
                testId = "test123",
                profileId = "profile123",
                wpm = 65.5,
                accuracy = 92.3,
                errors = listOf(5, 12, 18),
                timestamp = 1640995200000L,
            )

        assertEquals("test123", stats.testId)
        assertEquals("profile123", stats.profileId)
        assertEquals(65.5, stats.wpm, 0.01)
        assertEquals(92.3, stats.accuracy, 0.01)
        assertEquals(listOf(5, 12, 18), stats.errors)
        assertEquals(1640995200000L, stats.timestamp)
    }

    @Test
    fun `ProfileStatisticsListResponse with empty statistics should work`() {
        val response =
            ProfileStatisticsListResponse(
                profileId = "newprofile",
                statistics = emptyList(),
            )

        assertEquals("newprofile", response.profileId)
        assertTrue(response.statistics.isEmpty())
    }

    @Test
    fun `ProfileStatisticsListResponse with single test should work`() {
        val response =
            ProfileStatisticsListResponse(
                profileId = "singleprofile",
                statistics =
                    listOf(
                        StatisticsResponse(
                            testId = "test1",
                            profileId = "singleprofile",
                            wpm = 50.0,
                            accuracy = 100.0,
                            errors = emptyList(),
                            timestamp = 1640995200000L,
                        ),
                    ),
            )

        assertEquals("singleprofile", response.profileId)
        assertEquals(1, response.statistics.size)
        assertEquals("test1", response.statistics[0].testId)
        assertEquals(50.0, response.statistics[0].wpm, 0.01)
        assertEquals(100.0, response.statistics[0].accuracy, 0.01)
        assertTrue(response.statistics[0].errors.isEmpty())
    }

    @Test
    fun `StatisticsResponse with no errors should work`() {
        val stats =
            StatisticsResponse(
                testId = "perfect-test",
                profileId = "profile123",
                wpm = 100.0,
                accuracy = 100.0,
                errors = emptyList(),
                timestamp = 1640995200000L,
            )

        assertEquals("perfect-test", stats.testId)
        assertEquals(100.0, stats.wpm, 0.01)
        assertEquals(100.0, stats.accuracy, 0.01)
        assertTrue(stats.errors.isEmpty())
    }
}
