package com.udacity.asteroidradar.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.data.local.dao.AsteroidDao
import com.udacity.asteroidradar.data.model.Asteroid
import com.udacity.asteroidradar.utils.Constants


@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
abstract class DatabaseService : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseService? = null

        fun getInstance(context: Context): DatabaseService {
            synchronized(this)
            {
                var instace = INSTANCE
                if (instace == null) {
                    instace = Room.databaseBuilder(context.applicationContext,
                                    DatabaseService::class.java,
                                    Constants.DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE = instace
                }
                return instace
            }
        }
    }


}