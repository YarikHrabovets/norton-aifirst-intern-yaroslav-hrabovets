package com.example.securityhealthdashboard.ui.dashboard

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.securityhealthdashboard.R
import com.example.securityhealthdashboard.data.model.ScanState
import com.example.securityhealthdashboard.ui.dashboard.components.*
import com.example.securityhealthdashboard.ui.dashboard.scan.ScanProgressSheet
import com.example.securityhealthdashboard.ui.theme.BackgroundLight
import com.example.securityhealthdashboard.ui.theme.NortonBlack
import com.example.securityhealthdashboard.ui.theme.TextGray
import com.example.securityhealthdashboard.util.StatusColorMapper
import com.example.securityhealthdashboard.util.TimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    DashboardContent(
        uiState = uiState,
        onStartScan = viewModel::startScan,
        onDismissScanSheet = viewModel::dismissScanSheet
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    uiState: DashboardUiState,
    onStartScan: () -> Unit,
    onDismissScanSheet: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    if (uiState.showScanSheet) {
        val scanState = uiState.scanState
        val scanProgress = if (scanState is ScanState.Scanning) scanState.progress else 0f
                           
        ModalBottomSheet(
            onDismissRequest = onDismissScanSheet,
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
            containerColor = Color.White
        ) {
            val currentCategoryName = (uiState.scanState as? ScanState.Scanning)?.currentCategory

            ScanProgressSheet(
                progress = scanProgress,
                currentCategory = currentCategoryName,
                onCancel = onDismissScanSheet
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.dashboard_title), 
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = NortonBlack
                        )
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.shadow(4.dp)
            )
        },
        containerColor = BackgroundLight
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            uiState.report?.let { report ->
                val isScanning = uiState.scanState is ScanState.Scanning

                Spacer(modifier = Modifier.height(32.dp))

                // FIXED: Consistent container for ScoreRing to prevent layout jumps
                Box(
                    modifier = Modifier.height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (!isScanning) {
                        ScoreRing(
                            score = report.overallScore,
                            isScanning = false,
                            scanProgress = 0f
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                val statusTextRes = StatusColorMapper.getStatusTextRes(report.overallScore, uiState.isFirstScanPerformed)
                val statusText = stringResource(statusTextRes)
                val statusColor = if (!uiState.isFirstScanPerformed) NortonBlack 
                                  else StatusColorMapper.getColorForScore(report.overallScore.toFloat())

                Text(
                    text = if (isScanning) stringResource(R.string.scanning_threats)
                           else stringResource(R.string.status_device_status, statusText),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isScanning) NortonBlack else statusColor
                    )
                )

                Text(
                    text = if (isScanning) stringResource(R.string.scanning_wait)
                           else stringResource(R.string.last_scanned, TimeFormatter.formatLastScanned(report.lastScanned)),
                    color = TextGray,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(32.dp))

                ScanNowButton(
                    onClick = onStartScan,
                    isScanning = isScanning
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (uiState.isFirstScanPerformed) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        if (report.recommendations.isNotEmpty()) {
                            item {
                                Text(
                                    stringResource(R.string.action_required),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextGray
                                    ),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                            items(report.recommendations, key = { it.id }) { recommendation ->
                                RecommendationBanner(recommendation = recommendation)
                            }
                            item { Spacer(modifier = Modifier.height(16.dp)) }
                        }

                        item {
                            Text(
                                stringResource(R.string.security_categories),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextGray
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        items(report.categories, key = { it.id }) { category ->
                            CategoryCard(category = category)
                        }
                    }
                } else {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            stringResource(R.string.scan_description),
                            color = TextGray,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NortonBlack)
            }
        }
    }
}
