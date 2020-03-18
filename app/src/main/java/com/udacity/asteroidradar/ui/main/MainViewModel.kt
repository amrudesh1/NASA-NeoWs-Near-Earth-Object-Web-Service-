package com.udacity.asteroidradar.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.data.local.db.DatabaseService
import com.udacity.asteroidradar.data.model.Asteroid
import com.udacity.asteroidradar.data.model.PictureOfDay
import com.udacity.asteroidradar.data.remote.NetworkUtils
import com.udacity.asteroidradar.data.remote.RetrofitInstance
import kotlinx.coroutines.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val mainViewModelJob = Job()
    var asteriodData = MediatorLiveData<List<Asteroid>>()
    private val uiScope = CoroutineScope(Dispatchers.Main + mainViewModelJob)
    val databaseService: DatabaseService = DatabaseService.getInstance(application)
    val imageOfDay = MutableLiveData<PictureOfDay>()
    val startDate =
            SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))
    val endDate =
            SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis() + 604800000L))

    init {
        initData()
    }

    private fun initData() {
        uiScope.launch {
            setDataforAsteroid()
            setImageForTheDay()
        }
    }

    private fun setImageForTheDay() {
        val imagefortheDayMap: MutableMap<String, String> = HashMap()
        imagefortheDayMap["api_key"] = "JebcLPzxmR30MxO6zWCGbGWiaNeTOIZCSGkkOCZ8"
        GlobalScope.launch {
            val imageObject =
                    RetrofitInstance.appAPIWithMoshi.getImageForTheDay(imagefortheDayMap).await()
            setImagedata(imageObject)
        }
    }

    private suspend fun setImagedata(imageObject: PictureOfDay) {
        withContext(Dispatchers.Main)
        {
            imageOfDay.value = imageObject
        }
    }

    private suspend fun setDataforAsteroid() {
        val startDate =
                SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))
        val endDate =
                SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis() + 604800000L))


        val asteroidMap: MutableMap<String, String> =
                HashMap()
        asteroidMap["start_date"] = startDate
        asteroidMap["end_date"] = endDate
        asteroidMap["api_key"] = "JebcLPzxmR30MxO6zWCGbGWiaNeTOIZCSGkkOCZ8"
        GlobalScope.launch()
        {
            val dataObject =
                    JSONObject(RetrofitInstance.appAPIWithScalar.getAsteroids(asteroidMap).await())
            Log.i("Data", NetworkUtils.parseAsteroidsJsonResult(dataObject).size.toString())
            setLiveDataValueToDB(NetworkUtils.parseAsteroidsJsonResult(dataObject))
        }
    }

    private suspend fun setLiveDataValueToDB(parseAsteroidsJsonResult: ArrayList<Asteroid>) {
        withContext(Dispatchers.IO)
        {
            databaseService.asteroidDao.insertAsteroidData(parseAsteroidsJsonResult)
            setLiveData()
        }

    }


    private suspend fun setLiveData() {
        withContext(Dispatchers.Main)
        {
            asteriodData.value = getAsteroidsfromDb()
        }
    }

    private suspend fun getAsteroidsfromDb(): List<Asteroid> {
        return withContext(Dispatchers.IO)
        {
            val data = databaseService.asteroidDao.getAllAsteroidData(startDate, endDate)
            data
        }
    }

    fun getAsteriodData(): LiveData<List<Asteroid>> {
        return asteriodData
    }

    fun getCurrentImageData(): LiveData<PictureOfDay> {
        return imageOfDay
    }


    override fun onCleared() {
        super.onCleared()
        mainViewModelJob.cancel()
    }

}