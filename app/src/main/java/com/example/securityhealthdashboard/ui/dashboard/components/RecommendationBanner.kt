package com.example.securityhealthdashboard.ui.dashboard.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.securityhealthdashboard.R
import com.example.securityhealthdashboard.data.model.Recommendation
import com.example.securityhealthdashboard.data.model.Severity
import com.example.securityhealthdashboard.ui.theme.*
import com.example.securityhealthdashboard.util.SecurityDisplayMapper

@Composable
fun RecommendationBanner(
    recommendation: Recommendation,
    onAction: () -> Unit = {}
) {
    val (backgroundColor, accentColor) = when (recommendation.severity) {
        Severity.High -> CriticalRed.copy(alpha = 0.08f) to CriticalRed
        Severity.Medium -> WarningAmber.copy(alpha = 0.08f) to WarningAmber
        Severity.Low -> ScanBlue.copy(alpha = 0.08f) to ScanBlue
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.recommendation_label),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = accentColor,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = recommendation.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = NortonBlack,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(
                    text = stringResource(SecurityDisplayMapper.getActionLabelRes(recommendation.actionType)),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
