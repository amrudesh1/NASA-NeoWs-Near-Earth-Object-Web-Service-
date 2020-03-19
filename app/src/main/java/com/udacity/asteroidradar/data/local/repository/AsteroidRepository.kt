package com.udacity.asteroidradar.data.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.data.local.db.DatabaseService
import com.udacity.asteroidradar.data.local.db.asDomainModel
import com.udacity.asteroidradar.data.model.Asteroid
import com.udacity.asteroidradar.data.remote.NetworkUtils
import com.udacity.asteroidradar.data.remote.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: DatabaseService) {

    val startDate =
        SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))
    val endDate =
        SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis() + 604800000L))


    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAllAsteroidData(startDate, endDate))
        {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        val asteroidMap: MutableMap<String, String> =
            HashMap()
        asteroidMap["start_date"] = startDate
        asteroidMap["end_date"] = endDate
        asteroidMap["api_key"] = "JebcLPzxmR30MxO6zWCGbGWiaNeTOIZCSGkkOCZ8"

        val imagefortheDayMap: MutableMap<String, String> = HashMap()

        withContext(Dispatchers.IO)
        {
            val asteroids = RetrofitInstance.appAPIWithScalar.getAsteroids(asteroidMap).await()
            val imageForTheDay =
                RetrofitInstance.appAPIWithMoshi.getImageForTheDay(imagefortheDayMap)

            val dataObject = NetworkUtils.parseAsteroidsJsonResult(JSONObject(asteroids))
            database.asteroidDao.insertAsteroidData(dataObject)
        }
    }

}