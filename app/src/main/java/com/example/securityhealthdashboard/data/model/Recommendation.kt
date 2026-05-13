package com.example.securityhealthdashboard.data.model

data class Recommendation(
    val id: String,
    val titleRes: Int,
    val severity: Severity,
    val actionType: String
)
