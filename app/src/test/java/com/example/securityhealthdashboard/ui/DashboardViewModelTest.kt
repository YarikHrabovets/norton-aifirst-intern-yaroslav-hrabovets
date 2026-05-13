package com.example.securityhealthdashboard.ui

import com.example.securityhealthdashboard.data.model.*
import com.example.securityhealthdashboard.data.repository.SecurityRepository
import com.example.securityhealthdashboard.domain.usecase.*
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
                    SecurityCategory("1", 0, "OS_VERSION", CategoryStatus.Warning, 60, 0, Date()),
                    SecurityCategory("2", 0, "APP_THREATS", CategoryStatus.Safe, 95, 0, Date()),
                    SecurityCategory("3", 0, "WIFI_SAFETY", CategoryStatus.Safe, 90, 0, Date()),
                    SecurityCategory("4", 0, "PASSWORD_STRENGTH", CategoryStatus.Critical, 35, 0, Date()),
                    SecurityCategory("5", 0, "VPN_STATUS", CategoryStatus.Warning, 55, 0, Date()),
                    SecurityCategory("6", 0, "MALWARE_SCAN", CategoryStatus.Safe, 98, 0, Date())
                ),
                recommendations = listOf(
                    Recommendation("r1", 0, Severity.High, "FIX_PASSWORDS"),
                    Recommendation("r2", 0, Severity.Medium, "UPDATE_OS")
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
    fun `scan cancellation preserves previous report`() = runTest {
        val repo = MockRepo()
        val getReportUseCase = GetSecurityReportUseCase(repo)
        val startScanUseCase = StartSecurityScanUseCase()

        val viewModel = DashboardViewModel(getReportUseCase, startScanUseCase, testDispatcher, testDispatcher)
        advanceUntilIdle()
        
        val initialReport = viewModel.uiState.value.report

        viewModel.startScan()
        advanceTimeBy(100) // Start scanning
        
        viewModel.dismissScanSheet()
        advanceUntilIdle()

        assertEquals(ScanState.Idle, viewModel.uiState.value.scanState)
        assertEquals(initialReport, viewModel.uiState.value.report)
    }

    @Test
    fun `scan error state appears when process fails`() = runTest {
        val repo = MockRepo()
        val getReportUseCase = GetSecurityReportUseCase(repo)
        
        // Custom UseCase that fails
        val failingScanUseCase = object : StartSecurityScanUseCase() {
            override fun invoke(currentReport: SecurityReport) = kotlinx.coroutines.flow.flow<ScanProgressUpdate> {
                throw Exception("Scan Interrupted")
            }
        }

        val viewModel = DashboardViewModel(getReportUseCase, failingScanUseCase, testDispatcher, testDispatcher)
        advanceUntilIdle()
        
        viewModel.startScan()
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.error)
        assertEquals("Scan Interrupted", viewModel.uiState.value.error)
        assert(viewModel.uiState.value.scanState is ScanState.Failed)
    }
}
