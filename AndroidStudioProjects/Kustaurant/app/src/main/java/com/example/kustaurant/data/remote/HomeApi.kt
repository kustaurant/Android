package com.example.kustaurant.data.remote

import com.example.kustaurant.data.model.HomeResponse
import retrofit2.http.GET

interface HomeApi {
    @GET("api/v1/home")
    suspend fun getHome(): HomeResponse
}