package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.LoginResponse

interface NaverLoginRepository {
    suspend fun postNaverLogin(provider: String, providerId: String, naverAccessToken: String): LoginResponse
}