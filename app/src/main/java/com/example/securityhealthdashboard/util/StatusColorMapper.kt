package com.example.securityhealthdashboard.util

import androidx.compose.ui.graphics.Color
import com.example.securityhealthdashboard.R
import com.example.securityhealthdashboard.data.model.CategoryStatus
import com.example.securityhealthdashboard.ui.theme.*

object StatusColorMapper {
    fun getColorForStatus(status: CategoryStatus): Color {
        return when (status) {
            CategoryStatus.NotScanned -> TextGray
            CategoryStatus.Safe -> SafeGreen
            CategoryStatus.Warning -> WarningAmber
            CategoryStatus.Critical -> CriticalRed
            CategoryStatus.Scanning -> ScanBlue
        }
    }

    fun getColorForScore(score: Float): Color {
        return when {
            score >= 90 -> SafeGreen
            score >= 70 -> SafeGreen
            score >= 40 -> WarningAmber
            else -> CriticalRed
        }
    }

    fun getStatusTextRes(score: Int, isFirstScanPerformed: Boolean): Int {
        if (!isFirstScanPerformed) return R.string.status_scan_required
        return when {
            score >= 90 -> R.string.status_excellent
            score >= 70 -> R.string.status_secure
            score >= 40 -> R.string.status_needs_attention
            else -> R.string.status_at_risk
        }
    }
    
    fun getCategoryStatusTextRes(status: CategoryStatus): Int {
        return when (status) {
            CategoryStatus.NotScanned -> R.string.cat_status_not_scanned
            CategoryStatus.Safe -> R.string.cat_status_safe
            CategoryStatus.Warning -> R.string.cat_status_warning
            CategoryStatus.Critical -> R.string.cat_status_critical
            CategoryStatus.Scanning -> R.string.cat_status_scanning
        }
    }

    fun getCategoryDetailRes(status: CategoryStatus): Int {
        return when (status) {
            CategoryStatus.NotScanned -> R.string.cat_detail_scan_required
            CategoryStatus.Safe -> R.string.cat_detail_no_issues
            CategoryStatus.Warning -> R.string.cat_detail_attention
            CategoryStatus.Critical -> R.string.cat_detail_critical
            CategoryStatus.Scanning -> R.string.cat_detail_no_issues // Or a specific scanning detail
        }
    }
}
