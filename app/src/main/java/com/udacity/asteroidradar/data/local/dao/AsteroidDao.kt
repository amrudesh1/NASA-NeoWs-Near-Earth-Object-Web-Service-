package com.udacity.asteroidradar.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.data.model.Asteroid

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAsteroidData(list: List<Asteroid>)

    @Query("SELECT * FROM ASTEROID WHERE  closeApproachDate BETWEEN :dayst AND :dayen ORDER BY closeApproachDate ASC")
    fun getAllAsteroidData(dayst: String, dayen: String): List<Asteroid>
}