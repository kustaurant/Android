package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.remote.GoodByeApi
import com.kust.kustaurant.domain.repository.GoodByeRepository
import javax.inject.Inject

class GoodByeRepositoryImpl @Inject constructor(
    private val goodByeApi: GoodByeApi
):GoodByeRepository{
    override suspend fun postGoodBye(): String {
        return goodByeApi.postGoodBye().string()
    }
}