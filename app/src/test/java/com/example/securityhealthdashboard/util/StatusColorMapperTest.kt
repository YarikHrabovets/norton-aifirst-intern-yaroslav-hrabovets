package com.example.securityhealthdashboard.util

import androidx.compose.ui.graphics.Color
import com.example.securityhealthdashboard.data.model.CategoryStatus
import com.example.securityhealthdashboard.ui.theme.*
import org.junit.Assert.assertEquals
import org.junit.Test

class StatusColorMapperTest {

    @Test
    fun `getColorForStatus returns correct colors`() {
        assertEquals(SafeGreen, StatusColorMapper.getColorForStatus(CategoryStatus.Safe))
        assertEquals(WarningAmber, StatusColorMapper.getColorForStatus(CategoryStatus.Warning))
        assertEquals(CriticalRed, StatusColorMapper.getColorForStatus(CategoryStatus.Critical))
        assertEquals(ScanBlue, StatusColorMapper.getColorForStatus(CategoryStatus.Scanning))
    }

    @Test
    fun `getColorForScore returns correct colors`() {
        assertEquals(SafeGreen, StatusColorMapper.getColorForScore(85f))
        assertEquals(WarningAmber, StatusColorMapper.getColorForScore(65f))
        assertEquals(CriticalRed, StatusColorMapper.getColorForScore(35f))
    }
}
