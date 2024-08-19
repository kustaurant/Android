package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.remote.NaverLoginApi
import com.kust.kustaurant.domain.repository.NaverLoginRepository
import javax.inject.Inject

class NaverLoginRepositoryImpl @Inject constructor(
    private val naverloginApi: NaverLoginApi
): NaverLoginRepository {
    override suspend fun postNaverLogin(
        provider: String,
        providerId: String,
        naverAccessToken: String
    ): String {
        return naverloginApi.postNaverLogin(provider, providerId, naverAccessToken).string()
    }
}