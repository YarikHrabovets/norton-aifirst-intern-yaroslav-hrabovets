package com.example.securityhealthdashboard.data.model

import java.util.Date

/**
 * Data model representing the overall security health report of the device.
 *
 * @property id Unique identifier for the report.
 * @property overallScore Aggregated security score (0-100).
 * @property status Summary status message.
 * @property lastScanned Timestamp of the last successful scan.
 * @property categories List of detailed security categories (e.g., Wifi, Malware).
 * @property recommendations Actionable items to improve security.
 */
data class SecurityReport(
    val id: String,
    val overallScore: Int,
    val status: String,
    val lastScanned: Date,
    val categories: List<SecurityCategory>,
    val recommendations: List<Recommendation>
)
