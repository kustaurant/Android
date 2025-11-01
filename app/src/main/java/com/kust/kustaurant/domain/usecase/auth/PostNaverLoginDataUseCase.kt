package com.kust.kustaurant.domain.usecase.auth

import com.kust.kustaurant.data.model.LoginResponse
import com.kust.kustaurant.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostNaverLoginDataUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend fun invoke(provider: String, providerId: String, naverAccessToken: String):LoginResponse {
        return authRepository.postNaverLogin(provider, providerId, naverAccessToken)
    }
}