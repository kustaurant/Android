package com.kust.kustaurant.domain.repository

interface NaverLoginRepository {
    suspend fun postNaverLogin(provider: String, providerId: String, naverAccessToken: String):String
}