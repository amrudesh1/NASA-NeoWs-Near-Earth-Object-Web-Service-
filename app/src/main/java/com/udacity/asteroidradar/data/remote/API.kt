package com.udacity.asteroidradar.data.remote

import com.google.gson.JsonObject
import com.udacity.asteroidradar.data.model.Asteroid
import com.udacity.asteroidradar.utils.Constants
import kotlinx.coroutines.Deferred
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface API {
    @GET(Constants.REST_URL)
    fun getAsteroids(@QueryMap data: Map<String, String>): Deferred<String>
}