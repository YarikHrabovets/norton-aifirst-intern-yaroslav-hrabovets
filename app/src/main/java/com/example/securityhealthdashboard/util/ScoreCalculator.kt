package com.example.securityhealthdashboard.util

import com.example.securityhealthdashboard.data.model.SecurityCategory

object ScoreCalculator {
    fun calculateAverageScore(categories: List<SecurityCategory>): Int {
        if (categories.isEmpty()) return 0
        return categories.map { it.score }.average().toInt()
    }
}
