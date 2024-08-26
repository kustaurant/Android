package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.LoginResponse

interface NewAccessTokenRepository {
    suspend fun postNewAccessToken(): LoginResponse
}