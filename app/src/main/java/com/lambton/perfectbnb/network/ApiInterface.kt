package com.lambton.perfectbnb.network

import com.lambton.perfectbnb.models.ModalClass
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {


    @GET("weather")
    fun getCurrentWeatherData(
    @Query("lat")latitude:String,
        @Query("lon")longitude:String,
        @Query("appid")ApiiId:String,
    @Query("units")Unit:String):Call<ModalClass>
}