package com.example.kustaurant.data.remote

import com.example.kustaurant.data.model.DrawRestaurantData
import com.example.kustaurant.data.model.RestaurantResponse
import com.example.kustaurant.data.model.TierMapDataResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface KustaurantApi {
    @GET("/api/v1/tier/map")
    suspend fun getTierMapList(
        @Query("cuisines") cuisines: String,
        @Query("situations") situations: String,
        @Query("locations") locations: String
    ): TierMapDataResponse

    @GET("/api/v1/tier")
    suspend fun getRestaurantList(
        @Query("cuisines") cuisines: String,
        @Query("situations") situations: String,
        @Query("locations") locations: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 30
    ): List<RestaurantResponse>

    @GET("/api/v1/draw")
    suspend fun getDrawRestaurantsData(
        @Query("cuisines") cuisines: String,
        @Query("situations") situations: String,
        @Query("locations") locations: String
    ):  List<DrawRestaurantData>
}