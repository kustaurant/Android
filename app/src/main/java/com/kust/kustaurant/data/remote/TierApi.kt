package com.kust.kustaurant.data.remote

import com.kust.kustaurant.data.model.DrawRestaurant
import com.kust.kustaurant.data.model.tier.TierListResponse
import com.kust.kustaurant.data.model.tier.TierMapDataResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TierApi {
    @GET("/api/v2/tier")
    suspend fun getRestaurantList(
        @Query("cuisines") cuisines: String,
        @Query("situations") situations: String,
        @Query("locations") locations: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int = 30
    ): TierListResponse

    @GET("/api/v2/tier/map")
    suspend fun getTierMapList(
        @Query("cuisines") cuisines: String,
        @Query("situations") situations: String,
        @Query("locations") locations: String
    ): TierMapDataResponse

    @GET("/api/v2/draw")
    suspend fun getDrawRestaurantsData(
        @Query("cuisines") cuisines: String,
        @Query("locations") locations: String
    ):  List<DrawRestaurant>
}