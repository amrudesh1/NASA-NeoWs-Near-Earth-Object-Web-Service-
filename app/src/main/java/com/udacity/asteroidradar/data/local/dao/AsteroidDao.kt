package com.udacity.asteroidradar.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.data.model.Asteroid

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAsteroidData(list: ArrayList<Asteroid>)

    @Query("SELECT * FROM ASTEROID")
    fun getAllAsteroidData(): LiveData<List<Asteroid>>
}