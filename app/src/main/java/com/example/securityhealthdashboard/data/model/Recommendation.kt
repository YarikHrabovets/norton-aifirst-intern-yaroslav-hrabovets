package com.example.securityhealthdashboard.data.model

data class Recommendation(
    val id: String,
    val title: String,
    val severity: Severity,
    val actionType: String
)
