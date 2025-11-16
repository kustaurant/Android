package com.kust.kustaurant.data.remote

import retrofit2.Response
import retrofit2.http.POST

interface LogoutApi {
    @POST("/api/v2/auth/logout")
    suspend fun postLogout(): Response<String>
}