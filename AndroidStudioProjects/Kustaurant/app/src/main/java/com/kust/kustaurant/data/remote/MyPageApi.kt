package com.kust.kustaurant.data.remote

import com.kust.kustaurant.data.model.MyFavoriteResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface MyPageApi {
    @GET("/api/v1/auth/mypage/favorite-restaurant-list")
    suspend fun getFavoriteData(
        @Header("Authorization") Authorization : String
    ) : List<MyFavoriteResponse>
}