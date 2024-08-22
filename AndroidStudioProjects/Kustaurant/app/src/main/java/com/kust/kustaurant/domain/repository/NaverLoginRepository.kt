package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.NaverLoginResponse

interface NaverLoginRepository {
    suspend fun postNaverLogin(provider: String, providerId: String, naverAccessToken: String): NaverLoginResponse
}