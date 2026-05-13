package com.example.securityhealthdashboard.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.securityhealthdashboard.data.model.*
import com.example.securityhealthdashboard.di.qualifier.IoDispatcher
import com.example.securityhealthdashboard.di.qualifier.MainDispatcher
import com.example.securityhealthdashboard.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel responsible for the Security Dashboard UI logic.
 *
 * It manages the [DashboardUiState], handles initial data loading,
 * and coordinates the security scanning process.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getSecurityReportUseCase: GetSecurityReportUseCase,
    private val startSecurityScanUseCase: StartSecurityScanUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState(isLoading = true))
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private var scanJob: Job? = null
    private var lastStableReport: SecurityReport? = null

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch(mainDispatcher) {
            updateState { it.copy(isLoading = true, error = null) }
            try {
                val report = withContext(ioDispatcher) { getSecurityReportUseCase() }
                lastStableReport = report
                updateState { it.copy(
                    report = report,
                    isLoading = false,
                    isFirstScanPerformed = report.lastScanned.time > 0
                ) }
            } catch (e: Exception) {
                updateState { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    /**
     * Starts the security scan process with deterministic state updates.
     */
    fun startScan() {
        val currentReport = _uiState.value.report ?: return
        lastStableReport = currentReport

        scanJob?.cancel()
        scanJob = startSecurityScanUseCase(currentReport)
            .flowOn(ioDispatcher)
            .onStart {
                updateState { it.copy(
                    scanState = ScanState.Scanning(0f),
                    showScanSheet = true,
                    error = null
                ) }
            }
            .onEach { update ->
                handleScanUpdate(update)
            }
            .catch { e ->
                updateState { it.copy(
                    scanState = ScanState.Failed(e.message ?: "Unknown error"),
                    error = e.message
                ) }
            }
            .launchIn(viewModelScope)
    }

    private fun handleScanUpdate(update: ScanProgressUpdate) {
        when (update) {
            is ScanProgressUpdate.Started -> {
                updateState { it.copy(scanState = ScanState.Scanning(0f)) }
            }
            is ScanProgressUpdate.Progress -> {
                updateState { currentState ->
                    val updatedReport = currentState.report?.let { report ->
                        val newCategories = report.categories.map {
                            if (it.id == update.currentCategory.id) update.currentCategory else it
                        }
                        report.copy(categories = newCategories)
                    }
                    currentState.copy(
                        report = updatedReport,
                        scanState = ScanState.Scanning(update.progress, update.currentCategory.nameRes)
                    )
                }
            }
            is ScanProgressUpdate.Completed -> {
                lastStableReport = update.finalReport
                updateState { it.copy(
                    report = update.finalReport,
                    scanState = ScanState.Complete,
                    showScanSheet = false,
                    isFirstScanPerformed = true
                ) }
            }
        }
    }

    fun dismissScanSheet() {
        scanJob?.cancel()
        scanJob = null

        updateState {
            it.copy(
                report = lastStableReport ?: it.report,
                showScanSheet = false,
                scanState = ScanState.Idle
            )
        }
    }

    fun clearError() {
        updateState { it.copy(error = null) }
    }

    private fun updateState(reducer: (DashboardUiState) -> DashboardUiState) {
        _uiState.update(reducer)
    }
}
