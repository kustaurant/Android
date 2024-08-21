package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.remote.LogoutApi
import com.kust.kustaurant.domain.repository.LogoutRepository
import javax.inject.Inject

class LogoutRepositoryImpl @Inject constructor(
    private val logoutApi : LogoutApi
):LogoutRepository{
    override suspend fun postLogout(Authorization: String):String{
        return logoutApi.postLogout(Authorization).string()
    }
}