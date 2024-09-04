package com.kust.kustaurant.domain.repository

interface GoodByeRepository {
    suspend fun postGoodBye(): String
}