package com.example.securityhealthdashboard.util

import com.example.securityhealthdashboard.data.model.CategoryStatus
import com.example.securityhealthdashboard.data.model.SecurityCategory
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class ScoreCalculatorTest {

    @Test
    fun `calculateAverageScore returns correct average`() {
        val categories = listOf(
            SecurityCategory("1", "A", "", CategoryStatus.Safe, 100, "", Date()),
            SecurityCategory("2", "B", "", CategoryStatus.Safe, 50, "", Date())
        )
        
        val average = ScoreCalculator.calculateAverageScore(categories)
        assertEquals(75, average)
    }

    @Test
    fun `calculateAverageScore returns 0 for empty list`() {
        val average = ScoreCalculator.calculateAverageScore(emptyList())
        assertEquals(0, average)
    }
}
