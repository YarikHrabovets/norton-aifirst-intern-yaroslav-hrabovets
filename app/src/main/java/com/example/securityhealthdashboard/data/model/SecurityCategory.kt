package com.example.securityhealthdashboard.data.model

import java.util.Date

/**
 * Represents a specific security area being monitored (e.g., Wifi, OS version).
 *
 * @property id Unique identifier.
 * @property nameRes Resource ID for the display name of the category.
 * @property type Category type identifier (e.g., "WIFI_SAFETY").
 * @property status Current health status (Safe, Warning, Critical).
 * @property score Category-specific security score (0-100).
 * @property detailRes Resource ID for the user-facing detail message.
 * @property lastChecked Last time this specific category was evaluated.
 */
data class SecurityCategory(
    val id: String,
    val nameRes: Int,
    val type: String,
    val status: CategoryStatus,
    val score: Int,
    val detailRes: Int,
    val lastChecked: Date
)
