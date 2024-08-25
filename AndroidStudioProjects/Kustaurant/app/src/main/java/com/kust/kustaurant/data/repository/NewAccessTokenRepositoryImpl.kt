package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.remote.NewAccessTokenApi
import com.kust.kustaurant.domain.repository.NewAccessTokenRepository
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class NewAccessTokenRepositoryImpl @Inject constructor(
    private val newAccessTokenApi: NewAccessTokenApi
): NewAccessTokenRepository {
    override suspend fun postNewAccessToken(): Response<ResponseBody> {
        return newAccessTokenApi.postNewAccessToken()
    }
}