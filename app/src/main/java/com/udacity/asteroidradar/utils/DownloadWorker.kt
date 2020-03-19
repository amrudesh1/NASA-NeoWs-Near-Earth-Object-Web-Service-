package com.udacity.asteroidradar.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.data.local.db.DatabaseService
import com.udacity.asteroidradar.data.local.repository.AsteroidRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DownloadWorker private constructor(
    private val appContext: Context,
    private val workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        val WORK_NAME = "DOWNLOADWORKER"
    }

    override suspend fun doWork(): Result {
        val database = DatabaseService.getInstance(applicationContext)
        val repository = AsteroidRepository(database)
        try {
            repository.refreshAsteroids()
            return Result.success()
        } catch (e: HttpException) {
            return Result.retry()
        }

    }


}