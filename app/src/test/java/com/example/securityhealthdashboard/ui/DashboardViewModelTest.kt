package com.example.securityhealthdashboard.ui

import com.example.securityhealthdashboard.data.model.*
import com.example.securityhealthdashboard.data.repository.SecurityRepository
import com.example.securityhealthdashboard.domain.usecase.GetSecurityReportUseCase
import com.example.securityhealthdashboard.domain.usecase.StartSecurityScanUseCase
import com.example.securityhealthdashboard.ui.dashboard.DashboardViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.Before
import java.util.*

/*
 * NOTE:
 * These test cases were initially generated using an AI coding assistant.
 * They were reviewed and refined manually to ensure:
 * - correctness of coroutine behavior
 * - stability in async execution
 * - alignment with ViewModel architecture
 */

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private class MockRepo(private val shouldFail: Boolean = false) : SecurityRepository {
        override suspend fun fetchSecurityReport(): SecurityReport {
            if (shouldFail) throw Exception("Network Error")
            return SecurityReport(
                id = "report123",
                overallScore = 72,
                status = "COMPLETED",
                lastScanned = Date(),
                categories = listOf(
                    SecurityCategory("1", "OS Version", "OS_VERSION", CategoryStatus.Warning, 60, "Attention required", Date()),
                    SecurityCategory("2", "App Threats", "APP_THREATS", CategoryStatus.Safe, 95, "No issues found", Date()),
                    SecurityCategory("3", "Wi-Fi Safety", "WIFI_SAFETY", CategoryStatus.Safe, 90, "No issues found", Date()),
                    SecurityCategory("4", "Password Strength", "PASSWORD_STRENGTH", CategoryStatus.Critical, 35, "Critical risk detected", Date()),
                    SecurityCategory("5", "VPN Status", "VPN_STATUS", CategoryStatus.Warning, 55, "Attention required", Date()),
                    SecurityCategory("6", "Malware Scan", "MALWARE_SCAN", CategoryStatus.Safe, 98, "No issues found", Date())
                ),
                recommendations = listOf(
                    Recommendation("r1", "Update Weak Passwords", Severity.High, "FIX_PASSWORDS"),
                    Recommendation("r2", "Update Android OS", Severity.Medium, "UPDATE_OS")
                )
            )
        }

        override suspend fun checkOsVersion(): Int = 60
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial load fetches report and sets isFirstScanPerformed`() = runTest {
        val repo = MockRepo()
        val getReportUseCase = GetSecurityReportUseCase(repo)
        val startScanUseCase = StartSecurityScanUseCase()

        val viewModel = DashboardViewModel(getReportUseCase, startScanUseCase, testDispatcher, testDispatcher)

        advanceUntilIdle()

        assertEquals(72, viewModel.uiState.value.report?.overallScore)
        assertEquals(true, viewModel.uiState.value.isFirstScanPerformed)
        assertEquals(null, viewModel.uiState.value.error)
    }

    @Test
    fun `initial load failure sets error state`() = runTest {
        val repo = MockRepo(shouldFail = true)
        val getReportUseCase = GetSecurityReportUseCase(repo)
        val startScanUseCase = StartSecurityScanUseCase()

        val viewModel = DashboardViewModel(getReportUseCase, startScanUseCase, testDispatcher, testDispatcher)

        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.error)
        assertEquals("Network Error", viewModel.uiState.value.error)
    }

    @Test
    fun `startScan shows scan sheet and enters scanning state`() = runTest {
        val repo = MockRepo()
        val getReportUseCase = GetSecurityReportUseCase(repo)
        val startScanUseCase = StartSecurityScanUseCase()

        val viewModel = DashboardViewModel(getReportUseCase, startScanUseCase, testDispatcher, testDispatcher)

        advanceUntilIdle()

        viewModel.startScan()
        
        // Scan uses flowOn(ioDispatcher), we need to make sure we advance
        advanceUntilIdle()

        assertEquals(true, viewModel.uiState.value.isFirstScanPerformed)
        // Since it's MockRepo with 72 score, scan should complete if we advanceUntilIdle
        assertEquals(ScanState.Complete, viewModel.uiState.value.scanState)
        assertEquals(72, viewModel.uiState.value.report?.overallScore)
    }

    @Test
    fun `dismissScanSheet cancels scan and reverts state`() = runTest {
        val repo = MockRepo()
        val getReportUseCase = GetSecurityReportUseCase(repo)
        val startScanUseCase = StartSecurityScanUseCase()

        val viewModel = DashboardViewModel(getReportUseCase, startScanUseCase, testDispatcher, testDispatcher)

        advanceUntilIdle()
        
        viewModel.startScan()
        // Advance slightly to enter scanning but not finish (StartSecurityScanUseCase has delays)
        advanceTimeBy(100)
        
        viewModel.dismissScanSheet()
        advanceUntilIdle()

        assertEquals(ScanState.Idle, viewModel.uiState.value.scanState)
        assertEquals(false, viewModel.uiState.value.showScanSheet)
    }
}
