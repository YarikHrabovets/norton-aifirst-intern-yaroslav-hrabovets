package com.example.securityhealthdashboard.data.remote.dto

data class SecurityCategoryDto(
    val id: String,
    val name: String,
    val categoryType: String,
    val status: String,
    val score: Int,
    val detail: String,
    val lastChecked: Long
)
