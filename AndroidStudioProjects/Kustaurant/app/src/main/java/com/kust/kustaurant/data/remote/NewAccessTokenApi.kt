package com.kust.kustaurant.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.POST

interface NewAccessTokenApi {
    @POST("/api/v1/new-access-token")
    suspend fun postNewAccessToken(): Response<ResponseBody>
}