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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            SecurityHealthDashboardTheme {
                val viewModel: DashboardViewModel = hiltViewModel()
                AppNavGraph(viewModel = viewModel)
            }
        }
    }
}
