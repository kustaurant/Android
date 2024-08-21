package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.model.NaverLoginRequest
import com.kust.kustaurant.data.model.NaverLoginResponse
import com.kust.kustaurant.data.remote.NaverLoginApi
import com.kust.kustaurant.domain.repository.NaverLoginRepository
import javax.inject.Inject

class NaverLoginRepositoryImpl @Inject constructor(
    private val naverloginApi: NaverLoginApi
): NaverLoginRepository {
    override suspend fun postNaverLogin(provider: String, providerId: String, naverAccessToken: String): NaverLoginResponse {
        val request = NaverLoginRequest(provider,providerId,naverAccessToken)
        return naverloginApi.postNaverLogin(request)
    }
}