package com.kust.kustaurant.data.remote

import com.kust.kustaurant.data.model.LoginResponse
import retrofit2.http.POST

interface NewAccessTokenApi {
    @POST("/api/v1/new-access-token")
    suspend fun postNewAccessToken(): LoginResponse
}