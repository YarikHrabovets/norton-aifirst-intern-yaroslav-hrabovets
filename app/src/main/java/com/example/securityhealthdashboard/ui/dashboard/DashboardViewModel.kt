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

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getSecurityReportUseCase: GetSecurityReportUseCase,
    private val startSecurityScanUseCase: StartSecurityScanUseCase,
    @Named("IO") private val ioDispatcher: CoroutineDispatcher,
    @Named("Main") private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private var scanJob: Job? = null
    private var lastStableReport: SecurityReport? = null

    init {
        loadInitialData()
    }

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

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun updateCategoryInState(updatedCategory: SecurityCategory) {
        val currentReport = _uiState.value.report ?: return
        val newCategories = currentReport.categories.map {
            if (it.id == updatedCategory.id) updatedCategory else it
        }
        _uiState.update { it.copy(report = currentReport.copy(categories = newCategories)) }
    }
}
