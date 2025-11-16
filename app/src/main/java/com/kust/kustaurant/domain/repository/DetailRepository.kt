package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.data.model.CommentReplyResponse
import com.kust.kustaurant.data.model.DetailDataResponse
import com.kust.kustaurant.data.model.EvalCommentReactionResponse
import com.kust.kustaurant.data.model.EvaluationDataResponse
import com.kust.kustaurant.data.model.EvaluationReactionResponse
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
    suspend fun putEvalCommentReaction(
        evalCommentId: Int,
        reaction: String?
    ) : EvalCommentReactionResponse

    suspend fun putEvaluationReaction(
        evaluationId: Int,
        reaction: String?,
        userId: Int,
        role: String
    ) : EvaluationReactionResponse
}