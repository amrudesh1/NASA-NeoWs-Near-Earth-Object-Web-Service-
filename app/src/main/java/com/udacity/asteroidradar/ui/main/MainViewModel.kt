package com.udacity.asteroidradar.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.data.local.db.DatabaseService
import com.udacity.asteroidradar.data.local.repository.AsteroidRepository
import com.udacity.asteroidradar.data.model.Asteroid
import com.udacity.asteroidradar.data.model.PictureOfDay
import com.udacity.asteroidradar.data.remote.NetworkUtils
import com.udacity.asteroidradar.data.remote.RetrofitInstance
import kotlinx.coroutines.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val database = DatabaseService.getInstance(application)
    private val asteroidsRepository = AsteroidRepository(database)
    val imageOfDay = MutableLiveData<PictureOfDay>()


    init {
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()

            val imagefortheDayMap: MutableMap<String, String> = java.util.HashMap()
            imagefortheDayMap["api_key"] = "JebcLPzxmR30MxO6zWCGbGWiaNeTOIZCSGkkOCZ8"


            val imageData =
                RetrofitInstance.appAPIWithMoshi.getImageForTheDay(imagefortheDayMap).await()
            setImageData(imageData)
        }
    }

    private suspend fun setImageData(imageData: PictureOfDay) {
        withContext(Dispatchers.Main)
        {
            imageOfDay.value = imageData
        }
    }

    val asteroids = asteroidsRepository.asteroids


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }


}