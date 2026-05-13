package com.example.securityhealthdashboard.util

import com.example.securityhealthdashboard.R

object SecurityDisplayMapper {
    fun getIconForType(type: String): String {
        return when (type) {
            "OS_VERSION" -> "📱"
            "APP_THREATS" -> "🛡️"
            "WIFI_SAFETY" -> "📶"
            "PASSWORD_STRENGTH" -> "🔑"
            "VPN_STATUS" -> "🔒"
            "MALWARE_SCAN" -> "🦠"
            else -> "❓"
        }
    }

    fun getNameResForType(type: String): Int {
        return when (type) {
            "OS_VERSION" -> R.string.cat_os_version
            "APP_THREATS" -> R.string.cat_app_threats
            "WIFI_SAFETY" -> R.string.cat_wifi_safety
            "PASSWORD_STRENGTH" -> R.string.cat_password_strength
            "VPN_STATUS" -> R.string.cat_vpn_status
            "MALWARE_SCAN" -> R.string.cat_malware_scan
            else -> R.string.app_name
        }
    }

    fun getActionLabelRes(type: String): Int {
        return when (type) {
            "UPDATE_OS" -> R.string.action_update_now
            "FIX_PASSWORDS" -> R.string.action_fix_now
            "CONNECT_VPN" -> R.string.action_connect
            else -> R.string.action_review
        }
    }

    fun getRecommendationTitleRes(actionType: String): Int {
        return when (actionType) {
            "FIX_PASSWORDS" -> R.string.rec_update_passwords
            "UPDATE_OS" -> R.string.rec_update_os
            "CONNECT_VPN" -> R.string.rec_enable_vpn
            else -> R.string.recommendation_label
        }
    }
}
