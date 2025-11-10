package com.kust.kustaurant.data.remote

import com.kust.kustaurant.data.model.NaverLoginRequest
import com.kust.kustaurant.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/v2/login/naver")
    suspend fun postNaverLogin(
        @Body request: NaverLoginRequest
    ): LoginResponse

    @POST("/api/v2/auth/logout")
    suspend fun postLogout(): Response<Unit>

    @DELETE("/api/v2/auth/user")
    suspend fun deleteUser()
}