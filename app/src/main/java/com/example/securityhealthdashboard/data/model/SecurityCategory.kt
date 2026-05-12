package com.example.securityhealthdashboard.data.model

import java.util.Date

data class SecurityCategory(
    val id: String,
    val name: String,
    val type: String,
    val status: CategoryStatus,
    val score: Int,
    val detail: String,
    val lastChecked: Date
)
