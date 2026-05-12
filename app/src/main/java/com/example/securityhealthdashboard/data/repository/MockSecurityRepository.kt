package com.example.securityhealthdashboard.data.repository

import android.os.Build
import com.example.securityhealthdashboard.data.mapper.toSecurityReport
import com.example.securityhealthdashboard.data.model.SecurityReport
import com.example.securityhealthdashboard.data.remote.dto.*
import java.util.UUID

class MockSecurityRepository : SecurityRepository {
    override suspend fun fetchSecurityReport(): SecurityReport {
        // Simulate network delay
        kotlinx.coroutines.delay(500)
        
        val dto = SecurityReportDto(
            reportId = UUID.randomUUID().toString(),
            overallScore = 0,
            status = "INITIAL",
            lastScanned = 0L,
            categories = listOf(
                SecurityCategoryDto("1", "OS Version", "OS_VERSION", "NotScanned", 0, "Scan required", 0L),
                SecurityCategoryDto("2", "App Threats", "APP_THREATS", "NotScanned", 0, "Scan required", 0L),
                SecurityCategoryDto("3", "Wi-Fi Safety", "WIFI_SAFETY", "NotScanned", 0, "Scan required", 0L),
                SecurityCategoryDto("4", "Password Strength", "PASSWORD_STRENGTH", "NotScanned", 0, "Scan required", 0L),
                SecurityCategoryDto("5", "VPN Status", "VPN_STATUS", "NotScanned", 0, "Scan required", 0L),
                SecurityCategoryDto("6", "Malware Scan", "MALWARE_SCAN", "NotScanned", 0, "Scan required", 0L)
            ),
            recommendations = emptyList()
        )
        return dto.toSecurityReport()
    }

    override suspend fun checkOsVersion(): Int {
        // Real-device concept: Check if Android version is at least 13 (API 33)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) 100 else 60
    }
}
