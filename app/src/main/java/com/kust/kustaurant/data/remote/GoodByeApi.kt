package com.kust.kustaurant.data.remote

import okhttp3.ResponseBody
import retrofit2.http.POST

interface GoodByeApi {
    @POST("/api/v1/auth/goodbye-user")
    suspend fun postGoodBye():ResponseBody
}