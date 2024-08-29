package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.model.LoginResponse
import com.kust.kustaurant.data.remote.NewAccessTokenApi
import com.kust.kustaurant.domain.repository.NewAccessTokenRepository
import javax.inject.Inject

class NewAccessTokenRepositoryImpl @Inject constructor(
    private val newAccessTokenApi: NewAccessTokenApi
): NewAccessTokenRepository {
    override suspend fun postNewAccessToken(): LoginResponse {
        return newAccessTokenApi.postNewAccessToken()
    }
}