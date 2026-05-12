package com.example.securityhealthdashboard.domain.usecase

import com.example.securityhealthdashboard.data.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date

/**
 * Use Case responsible for simulating and performing a security scan.
 *
 * It iterates through security categories, calculates scores based on simulated logic,
 * and emits progress updates as a [Flow].
 */
class StartSecurityScanUseCase {

    /**
     * Executes the scan process.
     *
     * @param currentReport The initial report state to base the scan on.
     * @return A Flow of [ScanProgressUpdate] indicating the scan status and results.
     */
    operator fun invoke(currentReport: SecurityReport): Flow<ScanProgressUpdate> = flow {
        val categories = currentReport.categories
        val total = categories.size
        
        emit(ScanProgressUpdate.Started)
        
        val updatedCategories = categories.mapIndexed { index, category ->
            // Update UI with the category currently being scanned
            val progress = (index.toFloat()) / total
            emit(ScanProgressUpdate.Progress(progress, category.copy(status = CategoryStatus.Scanning)))
            
            delay(500) // Simulate processing time
            
            // Simulation logic for determining scores per category
            val newScore = when(category.type) {
                "OS_VERSION" -> 60
                "APP_THREATS" -> 95
                "WIFI_SAFETY" -> 90
                "PASSWORD_STRENGTH" -> 35
                "VPN_STATUS" -> 55
                "MALWARE_SCAN" -> 98
                else -> 80
            }
            
            // Map score to status and user-friendly detail message
            val newStatus = if (newScore < 50) CategoryStatus.Critical 
                            else if (newScore < 80) CategoryStatus.Warning 
                            else CategoryStatus.Safe
            
            val newDetail = if (newScore < 50) "Critical risk detected" 
                            else if (newScore < 80) "Attention required" 
                            else "No issues found"
            
            category.copy(
                status = newStatus, 
                score = newScore,
                detail = newDetail,
                lastChecked = Date()
            )
        }

        // Finalize progress
        emit(ScanProgressUpdate.Progress(1.0f, updatedCategories.last()))
        delay(500)
        
        // Calculate the aggregate score for the entire report
        val newScore = updatedCategories.map { it.score }.average().toInt()
        
        // Generate recommendations based on the scan findings
        val recommendations = mutableListOf<Recommendation>()
        if (updatedCategories.any { it.type == "PASSWORD_STRENGTH" && it.status == CategoryStatus.Critical }) {
            recommendations.add(Recommendation("r1", "Update Weak Passwords", Severity.High, "FIX_PASSWORDS"))
        }
        if (updatedCategories.any { it.type == "OS_VERSION" && it.status == CategoryStatus.Warning }) {
            recommendations.add(Recommendation("r2", "Update Android OS", Severity.Medium, "UPDATE_OS"))
        }

        val finalReport = currentReport.copy(
            overallScore = newScore,
            categories = updatedCategories,
            recommendations = recommendations,
            lastScanned = Date()
        )
        
        emit(ScanProgressUpdate.Completed(finalReport))
    }
}

/**
 * Represents states during the scan execution.
 */
sealed class ScanProgressUpdate {
    object Started : ScanProgressUpdate()
    data class Progress(val progress: Float, val currentCategory: SecurityCategory) : ScanProgressUpdate()
    data class Completed(val finalReport: SecurityReport) : ScanProgressUpdate()
}
