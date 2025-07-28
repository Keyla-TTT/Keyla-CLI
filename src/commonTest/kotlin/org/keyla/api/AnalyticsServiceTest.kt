package org.keyla.api

import org.keyla.models.*
import kotlin.test.*

class AnalyticsServiceTest {
    
    @Test
    fun `AnalyticsResponse should have correct structure`() {
        val analytics = AnalyticsResponse(
            userId = "user123",
            totalTests = 25,
            averageWpm = 65.5,
            averageAccuracy = 92.3,
            bestWpm = 85.0,
            worstWpm = 45.2,
            bestAccuracy = 98.5,
            worstAccuracy = 78.9,
            wpmImprovement = 12.5,
            accuracyImprovement = 5.2,
            totalErrors = 150,
            averageErrorsPerTest = 6.0
        )
        
        assertEquals("user123", analytics.userId)
        assertEquals(25, analytics.totalTests)
        assertEquals(65.5, analytics.averageWpm, 0.01)
        assertEquals(92.3, analytics.averageAccuracy, 0.01)
        assertEquals(85.0, analytics.bestWpm, 0.01)
        assertEquals(45.2, analytics.worstWpm, 0.01)
        assertEquals(98.5, analytics.bestAccuracy, 0.01)
        assertEquals(78.9, analytics.worstAccuracy, 0.01)
        assertEquals(12.5, analytics.wpmImprovement, 0.01)
        assertEquals(5.2, analytics.accuracyImprovement, 0.01)
        assertEquals(150, analytics.totalErrors)
        assertEquals(6.0, analytics.averageErrorsPerTest, 0.01)
    }
    
    @Test
    fun `AnalyticsResponse with zero tests should work`() {
        val analytics = AnalyticsResponse(
            userId = "newuser",
            totalTests = 0,
            averageWpm = 0.0,
            averageAccuracy = 0.0,
            bestWpm = 0.0,
            worstWpm = 0.0,
            bestAccuracy = 0.0,
            worstAccuracy = 0.0,
            wpmImprovement = 0.0,
            accuracyImprovement = 0.0,
            totalErrors = 0,
            averageErrorsPerTest = 0.0
        )
        
        assertEquals("newuser", analytics.userId)
        assertEquals(0, analytics.totalTests)
        assertEquals(0.0, analytics.averageWpm, 0.01)
        assertEquals(0.0, analytics.averageAccuracy, 0.01)
        assertEquals(0, analytics.totalErrors)
        assertEquals(0.0, analytics.averageErrorsPerTest, 0.01)
    }
    
    @Test
    fun `AnalyticsResponse with single test should work`() {
        val analytics = AnalyticsResponse(
            userId = "singleuser",
            totalTests = 1,
            averageWpm = 50.0,
            averageAccuracy = 100.0,
            bestWpm = 50.0,
            worstWpm = 50.0,
            bestAccuracy = 100.0,
            worstAccuracy = 100.0,
            wpmImprovement = 0.0,
            accuracyImprovement = 0.0,
            totalErrors = 0,
            averageErrorsPerTest = 0.0
        )
        
        assertEquals("singleuser", analytics.userId)
        assertEquals(1, analytics.totalTests)
        assertEquals(50.0, analytics.averageWpm, 0.01)
        assertEquals(100.0, analytics.averageAccuracy, 0.01)
        assertEquals(50.0, analytics.bestWpm, 0.01)
        assertEquals(50.0, analytics.worstWpm, 0.01)
        assertEquals(100.0, analytics.bestAccuracy, 0.01)
        assertEquals(100.0, analytics.worstAccuracy, 0.01)
        assertEquals(0, analytics.totalErrors)
    }
} 