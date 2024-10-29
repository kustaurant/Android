package com.kust.kustaurant.data.remote

import com.kust.kustaurant.data.model.NaverLoginRequest
import com.kust.kustaurant.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface NaverLoginApi {
    @POST("api/v1/naver-login")
    suspend fun postNaverLogin(
        @Body request: NaverLoginRequest
    ): LoginResponse
}