package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.data.model.DetailDataResponse

interface DetailRepository {
    suspend fun getDetailData(
        restaurantId : Int
    ): DetailDataResponse

    suspend fun getCommentData(
        restaurantId: Int, sort: String
    ) : List<CommentDataResponse>

    suspend fun postCommentData(
        restaurantId: Int, commentId: Int, inputText : String
    ) : List<CommentDataResponse>
}