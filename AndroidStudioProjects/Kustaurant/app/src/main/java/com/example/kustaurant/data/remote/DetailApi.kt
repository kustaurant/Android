package com.example.kustaurant.data.remote

import com.example.kustaurant.data.model.DetailDataResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailApi {
    @GET("/api/v1/restaurants/{restaurantId}")
    suspend fun getDetailData(
        @Path("restaurantId") restaurantId : Int
    ) : DetailDataResponse
}