package com.example.securityhealthdashboard.data.model

sealed class ScanState {
    object Idle : ScanState()
    data class Scanning(val progress: Float, val currentCategoryRes: Int? = null) : ScanState()
    object Complete : ScanState()
    data class Failed(val error: String) : ScanState()
}
