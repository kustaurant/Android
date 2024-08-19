package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.MyFavoriteResponse

interface MyPageRepository {
    suspend fun getFavoriteData(
        Authorization : String
    ): List<MyFavoriteResponse>
}