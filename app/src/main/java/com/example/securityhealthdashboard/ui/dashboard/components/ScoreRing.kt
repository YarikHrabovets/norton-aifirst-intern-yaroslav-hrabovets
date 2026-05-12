package com.example.securityhealthdashboard.ui.dashboard.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.securityhealthdashboard.ui.theme.ScanBlue
import com.example.securityhealthdashboard.ui.theme.SecurityHealthDashboardTheme
import com.example.securityhealthdashboard.util.StatusColorMapper

@Composable
fun ScoreRing(
    score: Int,
    modifier: Modifier = Modifier,
    isScanning: Boolean = false,
    scanProgress: Float = 0f
) {
    // Slightly faster animation when resetting to 0 at the start of a scan
    val animationDuration = if (isScanning && scanProgress == 0f) 600 else 1000

    val animatedScore by animateFloatAsState(
        targetValue = if (isScanning) scanProgress * 100 else score.toFloat(),
        animationSpec = tween(durationMillis = animationDuration),
        label = "ScoreAnimation"
    )

    val color = if (isScanning) {
        ScanBlue
    } else {
        StatusColorMapper.getColorForScore(animatedScore)
    }

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Canvas(modifier = Modifier.size(200.dp)) {
            // Track
            drawCircle(
                color = Color.LightGray.copy(alpha = 0.3f),
                style = Stroke(width = 14.dp.toPx())
            )
            // Progress Arc
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = (animatedScore / 100f) * 360f,
                useCenter = false,
                style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = "${animatedScore.toInt()}",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 56.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = color
                )
            )
            if (isScanning) {
                Text(
                    text = "%",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = color
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F7FA)
@Composable
fun ScoreRingPreview() {
    SecurityHealthDashboardTheme {
        ScoreRing(score = 85)
    }
}
