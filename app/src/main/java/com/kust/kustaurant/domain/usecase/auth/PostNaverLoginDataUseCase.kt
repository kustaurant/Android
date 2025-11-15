package com.kust.kustaurant.domain.usecase.auth

import com.kust.kustaurant.domain.model.auth.AuthToken
import com.kust.kustaurant.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostNaverLoginDataUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(provider: String, providerId: String, naverAccessToken: String): AuthToken {
        return authRepository.postNaverLogin(provider, providerId, naverAccessToken)
    }
}