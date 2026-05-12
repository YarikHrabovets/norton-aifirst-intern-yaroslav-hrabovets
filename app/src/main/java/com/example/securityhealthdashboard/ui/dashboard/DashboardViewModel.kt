package com.example.securityhealthdashboard.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.securityhealthdashboard.data.model.*
import com.example.securityhealthdashboard.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * ViewModel responsible for the Security Dashboard UI logic.
 *
 * It manages the [DashboardUiState], handles the initial data loading,
 * and coordinates the security scanning process using the provided use cases.
 *
 * @property getSecurityReportUseCase Fetches the current security status report.
 * @property startSecurityScanUseCase Initiates a scan and emits progress updates.
 * @property ioDispatcher For offloading database or network operations.
 * @property mainDispatcher For UI-related thread operations.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getSecurityReportUseCase: GetSecurityReportUseCase,
    private val startSecurityScanUseCase: StartSecurityScanUseCase,
    @Named("IO") private val ioDispatcher: CoroutineDispatcher,
    @Named("Main") private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())

    /**
     * Public UI state stream that the UI layer observes.
     */
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private var scanJob: Job? = null
    private var lastStableReport: SecurityReport? = null

    init {
        loadInitialData()
    }

    /**
     * Fetches the initial security report from the data source.
     */
    private fun loadInitialData() {
        viewModelScope.launch(mainDispatcher) {
            _uiState.update { it.copy(error = null) }
            try {
                val report = withContext(ioDispatcher) { getSecurityReportUseCase() }
                lastStableReport = report
                _uiState.update { it.copy(
                    report = report,
                    isFirstScanPerformed = report.lastScanned.time > 0
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    /**
     * Starts the security scan process.
     * Cancels any existing scan job and updates the state to 'Scanning'.
     */
    fun startScan() {
        val currentReport = _uiState.value.report ?: return
        lastStableReport = currentReport

        scanJob?.cancel()
        scanJob = startSecurityScanUseCase(currentReport)
            .flowOn(ioDispatcher)
            .onStart {
                _uiState.update { it.copy(
                    scanState = ScanState.Scanning(0f),
                    showScanSheet = true,
                    error = null
                ) }
            }
            .onEach { update ->
                when (update) {
                    is ScanProgressUpdate.Started -> {
                        _uiState.update { it.copy(scanState = ScanState.Scanning(0f)) }
                    }
                    is ScanProgressUpdate.Progress -> {
                        updateCategoryInState(update.currentCategory)
                        _uiState.update { it.copy(
                            scanState = ScanState.Scanning(update.progress, update.currentCategory.name)
                        ) }
                    }
                    is ScanProgressUpdate.Completed -> {
                        lastStableReport = update.finalReport
                        _uiState.update { it.copy(
                            report = update.finalReport,
                            scanState = ScanState.Complete,
                            showScanSheet = false,
                            isFirstScanPerformed = true
                        ) }
                    }
                }
            }
            .catch { e ->
                _uiState.update { it.copy(
                    scanState = ScanState.Failed(e.message ?: "Unknown error"),
                    error = e.message
                ) }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Cancels the scan and hides the scan bottom sheet.
     * Reverts to the last stable report to ensure UI consistency.
     */
    fun dismissScanSheet() {
        scanJob?.cancel()
        scanJob = null

        _uiState.update {
            it.copy(
                report = lastStableReport ?: it.report,
                showScanSheet = false,
                scanState = ScanState.Idle
            )
        }
    }

    /**
     * Clears the current error from the UI state.
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    /**
     * Helper to update specific security categories in the UI state while a scan is in progress.
     */
    private fun updateCategoryInState(updatedCategory: SecurityCategory) {
        val currentReport = _uiState.value.report ?: return
        val newCategories = currentReport.categories.map {
            if (it.id == updatedCategory.id) updatedCategory else it
        }
        _uiState.update { it.copy(report = currentReport.copy(categories = newCategories)) }
    }
}
