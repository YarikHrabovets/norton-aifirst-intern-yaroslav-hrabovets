package com.example.securityhealthdashboard.ui.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.securityhealthdashboard.data.model.CategoryStatus
import com.example.securityhealthdashboard.data.model.SecurityCategory
import com.example.securityhealthdashboard.ui.theme.CardWhite
import com.example.securityhealthdashboard.ui.theme.NortonBlack
import com.example.securityhealthdashboard.ui.theme.TextGray
import com.example.securityhealthdashboard.util.SecurityDisplayMapper
import com.example.securityhealthdashboard.util.StatusColorMapper

@Composable
fun CategoryCard(
    category: SecurityCategory,
    onClick: () -> Unit = {}
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Color.Black.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(12.dp),
        color = CardWhite
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = SecurityDisplayMapper.getIconForType(category.type),
                    fontSize = 24.sp
                )
            }
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(category.nameRes),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NortonBlack
                    )
                )
                Text(
                    text = stringResource(category.detailRes),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextGray
                    )
                )
            }
            
            StatusIndicator(status = category.status)
        }
    }
}

@Composable
fun StatusIndicator(status: CategoryStatus) {
    val color = StatusColorMapper.getColorForStatus(status)
    val textRes = StatusColorMapper.getCategoryStatusTextRes(status)

    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = stringResource(textRes).uppercase(),
            color = color,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 10.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
