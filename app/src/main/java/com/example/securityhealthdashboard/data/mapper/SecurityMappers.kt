package com.example.securityhealthdashboard.data.mapper

import com.example.securityhealthdashboard.data.model.*
import com.example.securityhealthdashboard.data.remote.dto.*
import com.example.securityhealthdashboard.util.SecurityDisplayMapper
import com.example.securityhealthdashboard.util.StatusColorMapper
import java.util.Date

fun SecurityCategoryDto.toSecurityCategory(): SecurityCategory {
    val domainStatus = enumValueOf<CategoryStatus>(status) ?: CategoryStatus.Safe
    return SecurityCategory(
        id = id,
        nameRes = SecurityDisplayMapper.getNameResForType(categoryType),
        type = categoryType,
        status = domainStatus,
        score = score,
        detailRes = StatusColorMapper.getCategoryDetailRes(domainStatus),
        lastChecked = Date(lastChecked)
    )
}

fun RecommendationDto.toRecommendation(): Recommendation {
    return Recommendation(
        id = id,
        titleRes = SecurityDisplayMapper.getRecommendationTitleRes(actionType),
        severity = enumValueOf<Severity>(severity) ?: Severity.Low,
        actionType = actionType
    )
}

fun SecurityReportDto.toSecurityReport(): SecurityReport {
    return SecurityReport(
        id = reportId,
        overallScore = overallScore,
        status = status,
        lastScanned = Date(lastScanned),
        categories = categories.map { it.toSecurityCategory() },
        recommendations = recommendations.map { it.toRecommendation() }
    )
}

// Inline helper because enumValueOf might throw if string doesn't match
private inline fun <reified T : Enum<T>> enumValueOf(name: String): T? {
    return try {
        java.lang.Enum.valueOf(T::class.java, name)
    } catch (e: Exception) {
        null
    }
}
