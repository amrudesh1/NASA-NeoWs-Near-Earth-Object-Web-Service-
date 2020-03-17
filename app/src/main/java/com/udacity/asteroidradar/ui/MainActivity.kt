package com.udacity.asteroidradar.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data.model.Asteroid
import com.udacity.asteroidradar.data.remote.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var asteroidCall: Call<Asteroid>
    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val asteroidMap: MutableMap<String, String> =
            HashMap()
        asteroidMap["start_date"] = "2020-03-17"
        asteroidMap["end_date"] = "2020-03-24"
        asteroidMap["api_key"] = "JebcLPzxmR30MxO6zWCGbGWiaNeTOIZCSGkkOCZ8"




    }
}
