package com.kust.kustaurant.domain.usecase.login

import com.kust.kustaurant.data.model.NaverLoginResponse
import com.kust.kustaurant.domain.repository.NaverLoginRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostNaverLoginDataUseCase @Inject constructor(
    private val naverLoginRepository: NaverLoginRepository
){
    suspend fun invoke(provider: String, providerId: String, naverAccessToken: String):NaverLoginResponse {
        return naverLoginRepository.postNaverLogin(provider, providerId, naverAccessToken)
    }
}