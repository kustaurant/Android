package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.model.MyFavoriteResponse
import com.kust.kustaurant.data.remote.MyPageApi
import com.kust.kustaurant.domain.repository.MyPageRepository
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageApi: MyPageApi
) : MyPageRepository {
    override suspend fun getFavoriteData(Authorization: String): List<MyFavoriteResponse> {
        return myPageApi.getFavoriteData(Authorization)
    }
}