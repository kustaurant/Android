package com.example.kustaurant.data.remote

import com.example.kustaurant.data.model.TierMapDataResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MapApi {
    @GET("/api/v1/tier/map")
    suspend fun getTierMapData(
        @Query("cuisines") cuisines: String,
        @Query("situations") situations: String,
        @Query("locations") locations: String
    ): TierMapDataResponse
}