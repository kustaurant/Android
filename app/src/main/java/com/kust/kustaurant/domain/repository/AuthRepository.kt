package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.LoginResponse

interface AuthRepository {
    suspend fun postNaverLogin(provider: String, providerId: String, naverAccessToken: String): LoginResponse

    suspend fun postLogout():String

    suspend fun deleteUser()
}