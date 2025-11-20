package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.remote.GoodByeApi
import com.kust.kustaurant.domain.repository.GoodByeRepository
import javax.inject.Inject

class GoodByeRepositoryImpl @Inject constructor(
    private val goodByeApi: GoodByeApi
): GoodByeRepository {
    override suspend fun deleteUser(): String {
        return try {
            val response = goodByeApi.deleteUser()
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