package com.kust.kustaurant.domain.repository
 
import com.kust.kustaurant.domain.model.auth.AuthToken

interface AuthRepository {
    suspend fun postNaverLogin(provider: String, providerId: String, naverAccessToken: String): AuthToken

    suspend fun postLogout():String

    suspend fun deleteUser()
}