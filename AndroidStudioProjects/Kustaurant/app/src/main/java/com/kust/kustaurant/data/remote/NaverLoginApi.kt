package com.kust.kustaurant.data.remote

import com.kust.kustaurant.data.model.NaverLoginRequest
import com.kust.kustaurant.data.model.NaverLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NaverLoginApi {
    @POST("api/v1/naver-login")
    suspend fun postNaverLogin(
        @Body request: NaverLoginRequest
    ): NaverLoginResponse
}