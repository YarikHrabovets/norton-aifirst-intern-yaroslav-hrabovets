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

    fun getActionLabelRes(type: String): Int {
        return when (type) {
            "UPDATE_OS" -> R.string.action_update_now
            "FIX_PASSWORDS" -> R.string.action_fix_now
            "CONNECT_VPN" -> R.string.action_connect
            else -> R.string.action_review
        }
    }
}
