package com.example.securityhealthdashboard.util

import com.example.securityhealthdashboard.R
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
        assertEquals(SafeGreen, StatusColorMapper.getColorForScore(95f))
        assertEquals(SafeGreen, StatusColorMapper.getColorForScore(78f))
        assertEquals(WarningAmber, StatusColorMapper.getColorForScore(58f))
        assertEquals(CriticalRed, StatusColorMapper.getColorForScore(28f))
    }

    @Test
    fun `getStatusTextRes returns correct resources based on score thresholds`() {
        // 0–39: At Risk
        assertEquals(R.string.status_at_risk, StatusColorMapper.getStatusTextRes(28, true))
        // 40–69: Needs Attention
        assertEquals(R.string.status_needs_attention, StatusColorMapper.getStatusTextRes(58, true))
        // 70–89: Secure
        assertEquals(R.string.status_secure, StatusColorMapper.getStatusTextRes(78, true))
        // 90–100: Excellent
        assertEquals(R.string.status_excellent, StatusColorMapper.getStatusTextRes(96, true))
    }
    
    @Test
    fun `getStatusTextRes returns Scan Required if first scan not performed`() {
        assertEquals(R.string.status_scan_required, StatusColorMapper.getStatusTextRes(96, false))
    }
}
