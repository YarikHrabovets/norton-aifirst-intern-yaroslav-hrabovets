package com.example.securityhealthdashboard.data.remote.dto

data class RecommendationDto(
    val id: String,
    val title: String,
    val severity: String,
    val actionType: String
)
