package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.model.NaverLoginRequest
import com.kust.kustaurant.data.model.toDomain
import com.kust.kustaurant.data.remote.AuthApi
import com.kust.kustaurant.domain.model.auth.AuthToken
import com.kust.kustaurant.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
): AuthRepository {
    override suspend fun postNaverLogin(provider: String, providerId: String, naverAccessToken: String): AuthToken {
        val request = NaverLoginRequest(provider,providerId,naverAccessToken)
        return authApi.postNaverLogin(request).toDomain()
    }

    override suspend fun postLogout():String{
        return authApi.postLogout().string()
    }

    override suspend fun deleteUser() {
        authApi.deleteUser().toString()
    }
}