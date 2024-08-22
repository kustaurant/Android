package com.kust.kustaurant.data.remote

import okhttp3.ResponseBody
import retrofit2.http.Header
import retrofit2.http.POST

interface LogoutApi {
    @POST("/api/v1/auth/logout")
    suspend fun postLogout(
        @Header("Authorization") Authorization : String
    ): ResponseBody
}