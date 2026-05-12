package com.example.securityhealthdashboard.data.repository

import com.example.securityhealthdashboard.data.model.SecurityReport

interface SecurityRepository {
    suspend fun fetchSecurityReport(): SecurityReport
    suspend fun checkOsVersion(): Int // Returns score 0-100
}
