package com.example.securityhealthdashboard.util

import java.text.SimpleDateFormat
import java.util.*

object TimeFormatter {
    private val dashboardDateFormatter = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())

    fun formatLastScanned(date: Date): String {
        if (date.time == 0L) return "Never"
        return dashboardDateFormatter.format(date)
    }
}
