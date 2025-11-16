package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.remote.LogoutApi
import com.kust.kustaurant.domain.repository.LogoutRepository
import javax.inject.Inject

class LogoutRepositoryImpl @Inject constructor(
    private val logoutApi : LogoutApi
):LogoutRepository{
    override suspend fun postLogout():String{
        return try {
            val response = logoutApi.postLogout()
            if (response.isSuccessful) {
                response.body() ?: ""
            } else {
                response.errorBody()?.string() ?: ""
            }
        } catch (e: Exception) {
            ""
        }
    }
}