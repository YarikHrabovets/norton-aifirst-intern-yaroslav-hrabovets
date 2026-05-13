package com.example.securityhealthdashboard.util

import com.example.securityhealthdashboard.R
import com.example.securityhealthdashboard.data.model.CategoryStatus
import com.example.securityhealthdashboard.data.model.SecurityCategory
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class ScoreCalculatorTest {

    @Test
    fun `calculateAverageScore returns correct average`() {
        val categories = listOf(
            SecurityCategory("1", R.string.cat_os_version, "OS_VERSION", CategoryStatus.Safe, 100, R.string.cat_detail_no_issues, Date()),
            SecurityCategory("2", R.string.cat_wifi_safety, "WIFI_SAFETY", CategoryStatus.Safe, 50, R.string.cat_detail_no_issues, Date())
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
