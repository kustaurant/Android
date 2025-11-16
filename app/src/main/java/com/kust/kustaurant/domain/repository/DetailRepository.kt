package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.data.model.CommentLikeResponse
import com.kust.kustaurant.data.model.CommentReplyResponse
import com.kust.kustaurant.data.model.DetailDataResponse
import com.kust.kustaurant.data.model.EvaluationDataResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface DetailRepository {
    suspend fun getDetailData(
        restaurantId : Int
    ): DetailDataResponse

    suspend fun getCommentData(
        restaurantId: Int, sort: String
    ) : List<CommentDataResponse>

    suspend fun postCommentReplyData(
        restaurantId: Int, evalCommentId: Int, body: String
    ) : CommentReplyResponse

    suspend fun postFavoriteToggle(
        restaurantId: Int
    ) : Boolean

    suspend fun getEvaluationData(
        restaurantId: Int
    ) : EvaluationDataResponse

    suspend fun postEvaluationData(
        restaurantId: Int,
        evaluationScore: RequestBody,
        evaluationSituations: List<MultipartBody.Part>,
        evaluationComment: RequestBody?,
        newImage: MultipartBody.Part?
    ) : List<CommentDataResponse>

    suspend fun deleteCommentData(
        restaurantId: Int,
        commentId: Int
    )

    suspend fun postCommentReport(
        restaurantId: Int,
        commentId: Int
    )
    suspend fun postCommentLike(
        restaurantId: Int,
        commentId: Int
    ) : CommentLikeResponse

    suspend fun postCommentDiskLike(
        restaurantId: Int,
        commentId: Int
    ) : CommentLikeResponse
}