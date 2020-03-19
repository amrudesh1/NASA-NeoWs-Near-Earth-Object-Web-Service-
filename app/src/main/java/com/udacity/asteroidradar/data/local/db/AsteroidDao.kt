package com.udacity.asteroidradar.data.local.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.data.model.Asteroid
import com.udacity.asteroidradar.utils.Constants

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroidData(list: ArrayList<AsteroidDatabase>)

    @Query("SELECT * FROM AsteroidDatabase WHERE  closeApproachDate BETWEEN :dayst AND :dayen ORDER BY closeApproachDate ASC")
    fun getAllAsteroidData(dayst: String, dayen: String): LiveData<List<AsteroidDatabase>>
}

@Database(entities = [AsteroidDatabase::class], version = 1, exportSchema = false)
abstract class DatabaseService : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseService? = null

        fun getInstance(context: Context): DatabaseService {
            synchronized(this)
            {
                var instace =
                    INSTANCE
                if (instace == null) {
                    instace = Room.databaseBuilder(
                            context.applicationContext,
                            DatabaseService::class.java,
                            Constants.DB_NAME
                        )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instace
                }
                return instace
            }
        }
    }


}