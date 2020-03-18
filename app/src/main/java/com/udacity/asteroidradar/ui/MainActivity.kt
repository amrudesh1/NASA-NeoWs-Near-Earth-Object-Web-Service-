package com.udacity.asteroidradar.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.utils.DownloadWorker
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val constraints = Constraints.Builder()
//                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val job = PeriodicWorkRequest.Builder(DownloadWorker::class.java, 10,
                TimeUnit.SECONDS).setConstraints(constraints).build()
//        val job = OneTimeWorkRequest.Builder(DownloadWorker::class.java)
//                .setConstraints(constraints)
//                .build()

        WorkManager.getInstance(this).enqueue(job)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(job.id).observe(this, Observer {
            Log.i("TAG", it.state.toString())
        })


    }
}
