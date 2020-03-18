package com.udacity.asteroidradar.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.data.local.db.DatabaseService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DownloadWorker private constructor(private val appContext: Context, private val workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {
    private val modelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + modelJob)
    var databaseService = DatabaseService.getInstance(appContext)


    override fun doWork(): Result {
        Log.i("DataTable", "Cleared")

        uiScope.launch {
            //  databaseService.clearAllTables()
        }
        return Result.success()
    }


}