package com.kust.kustaurant.domain.repository

interface LogoutRepository {
    suspend fun postLogout():String
}