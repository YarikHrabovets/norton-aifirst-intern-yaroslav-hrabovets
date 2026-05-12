package com.example.securityhealthdashboard.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.securityhealthdashboard.data.model.SecurityCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    category: SecurityCategory,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(category.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        // Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        Text("<", color = Color.White) // Simple placeholder
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(category.detail, style = MaterialTheme.typography.bodyLarge)
            // Add more details, history, etc.
        }
    }
}
