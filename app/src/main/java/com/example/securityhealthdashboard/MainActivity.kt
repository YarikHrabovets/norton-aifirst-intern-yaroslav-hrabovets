package com.example.securityhealthdashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.securityhealthdashboard.ui.dashboard.DashboardViewModel
import com.example.securityhealthdashboard.ui.navigation.AppNavGraph
import com.example.securityhealthdashboard.ui.theme.SecurityHealthDashboardTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main entry point of the application.
 *
 * This activity sets up the Compose environment, applies the app theme,
 * and initializes the navigation graph with the shared [DashboardViewModel].
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge support for modern Android UI
        enableEdgeToEdge()
        
        setContent {
            SecurityHealthDashboardTheme {
                // Shared ViewModel instance provided by Hilt
                val viewModel: DashboardViewModel = hiltViewModel()
                
                // Root navigation component
                AppNavGraph(viewModel = viewModel)
            }
        }
    }
}
