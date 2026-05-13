package com.example.securityhealthdashboard.ui.dashboard

import com.example.securityhealthdashboard.data.model.ScanState
import com.example.securityhealthdashboard.data.model.SecurityReport

data class DashboardUiState(
    val report: SecurityReport? = null,
    val scanState: ScanState = ScanState.Idle,
    val isFirstScanPerformed: Boolean = false,
    val showScanSheet: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
