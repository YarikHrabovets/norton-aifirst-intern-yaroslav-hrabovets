package com.example.securityhealthdashboard.data.model

import java.util.Date

/**
 * Represents a specific security area being monitored (e.g., Wifi, OS version).
 *
 * @property id Unique identifier.
 * @property name Display name of the category.
 * @property type Category type identifier (e.g., "WIFI_SAFETY").
 * @property status Current health status (Safe, Warning, Critical).
 * @property score Category-specific security score (0-100).
 * @property detail User-facing detail message about the current status.
 * @property lastChecked Last time this specific category was evaluated.
 */
data class SecurityCategory(
    val id: String,
    val name: String,
    val type: String,
    val status: CategoryStatus,
    val score: Int,
    val detail: String,
    val lastChecked: Date
)
