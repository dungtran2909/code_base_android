package com.dungtran.codebase.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dungtran.codebase.data.worker.SyncDataWorker
import com.dungtran.codebase.domain.usecase.ObserveProductsUseCase
import com.dungtran.codebase.domain.usecase.SyncProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeProducts: ObserveProductsUseCase,
    private val syncProducts: SyncProductsUseCase,
    private val workManager: WorkManager,
) : ViewModel() {

    private val syncing = MutableStateFlow(false)
    private val error = MutableStateFlow<String?>(null)

    val uiState: StateFlow<HomeUiState> =
        combine(
            observeProducts(),
            syncing,
            error,
        ) { products, isSyncing, errorMessage ->
            HomeUiState(
                isSyncing = isSyncing,
                products = products,
                errorMessage = errorMessage,
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    init {
        viewModelScope.launch {
            syncing.value = true
            error.value = null
            val result = syncProducts()
            syncing.value = false
            error.value = result.exceptionOrNull()?.message
        }
    }

    fun syncViaWorkManager() {
        val request = OneTimeWorkRequestBuilder<SyncDataWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        workManager.enqueueUniqueWork(
            UNIQUE_SYNC_WORK,
            ExistingWorkPolicy.REPLACE,
            request,
        )
    }

    companion object {
        private const val UNIQUE_SYNC_WORK = "sync_products_work"
    }
}

