package com.example.securityhealthdashboard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.securityhealthdashboard.ui.dashboard.DashboardScreen
import com.example.securityhealthdashboard.ui.dashboard.DashboardViewModel

@Composable
fun AppNavGraph(
    viewModel: DashboardViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("dashboard") {
            DashboardScreen(viewModel = viewModel)
        }
        // Add more destinations here later
    }
}
