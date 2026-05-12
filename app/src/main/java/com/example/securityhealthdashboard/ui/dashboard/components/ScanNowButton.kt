package com.example.securityhealthdashboard.ui.dashboard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.securityhealthdashboard.ui.theme.NortonBlack
import com.example.securityhealthdashboard.ui.theme.NortonYellow

@Composable
fun ScanNowButton(
    onClick: () -> Unit,
    isScanning: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = !isScanning,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = NortonYellow,
            contentColor = NortonBlack,
            disabledContainerColor = NortonYellow.copy(alpha = 0.5f),
            disabledContentColor = NortonBlack.copy(alpha = 0.5f)
        ),
        border = BorderStroke(1.5.dp, NortonBlack),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = if (isScanning) "SCAN IN PROGRESS" else "SCAN NOW",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        )
    }
}
