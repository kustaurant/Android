package com.kust.kustaurant.data.remote

import com.kust.kustaurant.data.model.DrawRestaurantData
import com.kust.kustaurant.data.model.RestaurantResponse
import com.kust.kustaurant.data.model.TierMapDataResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TierApi {
    @GET("/api/v1/auth/tier/map")
    suspend fun getAuthTierMapList(
        @Query("cuisines") cuisines: String,
        @Query("situations") situations: String,
        @Query("locations") locations: String
    ): TierMapDataResponse

    @GET("/api/v1/auth/tier")
    suspend fun getAuthRestaurantList(
        @Query("cuisines") cuisines: String,
        @Query("situations") situations: String,
        @Query("locations") locations: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int = 30
    ): List<RestaurantResponse>

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
        @Query("page") page: Int,
        @Query("limit") limit: Int = 30
    ): List<RestaurantResponse>

    @GET("/api/v1/draw")
    suspend fun getDrawRestaurantsData(
        @Query("cuisines") cuisines: String,
        @Query("locations") locations: String
    ):  List<DrawRestaurantData>
}