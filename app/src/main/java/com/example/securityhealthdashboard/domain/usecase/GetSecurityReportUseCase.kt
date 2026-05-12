package com.example.securityhealthdashboard.domain.usecase

import com.example.securityhealthdashboard.data.model.SecurityReport
import com.example.securityhealthdashboard.data.repository.SecurityRepository

class GetSecurityReportUseCase(private val repository: SecurityRepository) {
    suspend operator fun invoke(): SecurityReport {
        return repository.fetchSecurityReport()
    }
}
