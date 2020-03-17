package com.udacity.asteroidradar.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.data.local.db.DatabaseService
import com.udacity.asteroidradar.data.model.Asteroid
import com.udacity.asteroidradar.data.remote.NetworkUtils
import com.udacity.asteroidradar.data.remote.RetrofitInstance
import kotlinx.coroutines.*
import org.json.JSONObject

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val mainViewModelJob = Job()
    var asteriodData = MutableLiveData<List<Asteroid>>()
    private val uiScope = CoroutineScope(Dispatchers.Main + mainViewModelJob)
    val databaseService: DatabaseService = DatabaseService.getInstance(application)
    lateinit var data: LiveData<List<Asteroid>>

    init {
        initData()
    }

    private fun initData() {
        uiScope.launch {
            setDataforAsteroid()
        }
    }

    private suspend fun setDataforAsteroid() {
        val asteroidMap: MutableMap<String, String> =
                HashMap()
        asteroidMap["start_date"] = "2020-03-18"
        asteroidMap["end_date"] = "2020-03-25"
        asteroidMap["api_key"] = "JebcLPzxmR30MxO6zWCGbGWiaNeTOIZCSGkkOCZ8"
        GlobalScope.launch()
        {
            val dataObject = JSONObject(RetrofitInstance.appAPIWithScalar.getAsteroids(asteroidMap).await())
            Log.i("Data", NetworkUtils.parseAsteroidsJsonResult(dataObject).size.toString())
            setLiveDataValueToDB(NetworkUtils.parseAsteroidsJsonResult(dataObject))
        }
    }

    private suspend fun setLiveDataValueToDB(parseAsteroidsJsonResult: ArrayList<Asteroid>) {
        withContext(Dispatchers.IO)
        {
            databaseService.asteroidDao.insertAsteroidData(parseAsteroidsJsonResult)
            getLiveDataFromDB()
        }

    }

    private suspend fun getLiveDataFromDB() {
        withContext(Dispatchers.IO)
        {
            setLiveData(databaseService.asteroidDao.getAllAsteroidData())

        }
    }

    private suspend fun setLiveData(allAsteroidData: LiveData<List<Asteroid>>) {
        withContext(Dispatchers.Main)
        {
            asteriodData.value = getAsteroidsfromDb()!!.value
        }
    }

    private suspend fun getAsteroidsfromDb(): LiveData<List<Asteroid>>? {
        return withContext(Dispatchers.IO)
        {
            val data = databaseService.asteroidDao.getAllAsteroidData()
            data
        }
    }

    fun getAsteriodData(): LiveData<List<Asteroid>> {
        return asteriodData
    }


    override fun onCleared() {
        super.onCleared()
        mainViewModelJob.cancel()
    }

}