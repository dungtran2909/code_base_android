package com.dungtran.codebase.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dungtran.codebase.domain.usecase.SyncProductsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val syncProducts: SyncProductsUseCase,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result =
        syncProducts().fold(
            onSuccess = { Result.success() },
            onFailure = { Result.retry() },
        )
}

