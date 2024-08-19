package com.kust.kustaurant.data.remote

import okhttp3.ResponseBody
import retrofit2.http.POST
import retrofit2.http.Query

interface NaverLoginApi {
    @POST("api/v1/naver-login")
    suspend fun postNaverLogin(
        @Query("provider") provider: String,
        @Query("providerId") providerId: String,
        @Query("naverAccessToken") naverAccessToken: String
    ): ResponseBody
}