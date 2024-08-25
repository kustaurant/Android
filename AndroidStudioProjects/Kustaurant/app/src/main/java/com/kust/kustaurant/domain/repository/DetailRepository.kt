package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.data.model.DetailDataResponse
import com.kust.kustaurant.data.model.EvaluationDataRequest
import com.kust.kustaurant.data.model.EvaluationDataResponse

interface DetailRepository {
    suspend fun getDetailData(
        restaurantId : Int
    ): DetailDataResponse

    suspend fun getCommentData(
        restaurantId: Int, sort: String
    ) : List<CommentDataResponse>

    suspend fun postCommentData(
        restaurantId: Int, commentId: Int, inputText : String
    ) : CommentDataResponse

    suspend fun postFavoriteToggle(
        restaurantId: Int
    ) : Boolean

    suspend fun getEvaluationData(
        restaurantId: Int
    ) : EvaluationDataResponse

    suspend fun postEvaluationData(
        restaurantId: Int,
        request : EvaluationDataRequest
    ) : DetailDataResponse
}