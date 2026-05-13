package com.example.securityhealthdashboard.ui.dashboard.scan

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
import com.example.securityhealthdashboard.R
import com.example.securityhealthdashboard.ui.dashboard.components.ScoreRing
import com.example.securityhealthdashboard.ui.theme.NortonBlack
import com.example.securityhealthdashboard.ui.theme.TextGray

@Composable
fun ScanProgressSheet(
    progress: Float,
    currentCategoryRes: Int?,
    onCancel: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.scan_in_progress),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = NortonBlack
                )
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            ScoreRing(
                score = 0,
                isScanning = true,
                scanProgress = progress
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = currentCategoryRes?.let { stringResource(R.string.checking_category, stringResource(it)) }
                       ?: stringResource(R.string.initializing),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextGray
                )
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = NortonBlack
                )
            ) {
                Text(stringResource(R.string.cancel_scan), fontWeight = FontWeight.Bold)
            }
        }
    }
}
