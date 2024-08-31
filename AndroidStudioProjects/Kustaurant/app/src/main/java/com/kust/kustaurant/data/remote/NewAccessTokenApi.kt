package com.kust.kustaurant.data.remote

import com.kust.kustaurant.data.model.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface NewAccessTokenApi {
    @POST("/api/v1/new-access-token")
    fun refreshToken(@Header("Authorization") expiredToken : String): Call<ResponseBody>
}