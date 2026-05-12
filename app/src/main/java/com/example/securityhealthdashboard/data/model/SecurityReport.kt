package com.example.securityhealthdashboard.data.model

import java.util.Date

data class SecurityReport(
    val id: String,
    val overallScore: Int,
    val status: String,
    val lastScanned: Date,
    val categories: List<SecurityCategory>,
    val recommendations: List<Recommendation>
)
