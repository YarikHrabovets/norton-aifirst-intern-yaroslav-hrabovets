package com.example.securityhealthdashboard.data.remote.dto

data class SecurityReportDto(
    val reportId: String,
    val overallScore: Int,
    val status: String,
    val lastScanned: Long,
    val categories: List<SecurityCategoryDto>,
    val recommendations: List<RecommendationDto>
)
