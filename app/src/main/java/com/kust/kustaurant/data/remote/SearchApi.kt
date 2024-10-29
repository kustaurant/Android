package com.kust.kustaurant.data.remote

import com.kust.kustaurant.domain.model.SearchRestaurant
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("/api/v1/search")
    suspend fun getSearch(
        @Query("kw") kw: String
    ): List<SearchRestaurant>
}