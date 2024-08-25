package com.kust.kustaurant.domain.repository

import okhttp3.ResponseBody
import retrofit2.Response

interface NewAccessTokenRepository {
    suspend fun postNewAccessToken(): Response<ResponseBody>
}