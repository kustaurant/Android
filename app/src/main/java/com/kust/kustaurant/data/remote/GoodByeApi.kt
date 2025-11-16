package com.kust.kustaurant.data.remote

import retrofit2.Response
import retrofit2.http.DELETE

interface GoodByeApi {
    @DELETE("/api/v2/auth/user")
    suspend fun deleteUser(): Response<String>
}