package com.kust.kustaurant.data.repository


import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.data.model.CommentReplyRequest
import com.kust.kustaurant.data.model.CommentReplyResponse
import com.kust.kustaurant.data.model.DetailDataResponse
import com.kust.kustaurant.data.model.EvalCommentReactionResponse
import com.kust.kustaurant.data.model.EvaluationDataResponse
import com.kust.kustaurant.data.model.EvaluationReactionResponse
import com.kust.kustaurant.data.model.FavoriteResponse
import com.kust.kustaurant.data.remote.DetailApi
import com.kust.kustaurant.domain.repository.DetailRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val detailApi: DetailApi
) : DetailRepository {
    override suspend fun getDetailData(restaurantId: Int) : DetailDataResponse{
        return detailApi.getDetailData(restaurantId)
    }

    override suspend fun getCommentData(restaurantId: Int, sort: String) : List<CommentDataResponse>{
        return detailApi.getCommentData(restaurantId, sort)
    }

    override suspend fun postCommentReplyData(restaurantId: Int, evalCommentId: Int, body: String) : CommentReplyResponse{
        val request = CommentReplyRequest(body)
        return detailApi.postCommentReplyData(restaurantId, evalCommentId, request)
    }

    override suspend fun putFavorite(restaurantId: Int) : FavoriteResponse {
        return detailApi.putFavorite(restaurantId)
    }

    override suspend fun deleteFavorite(restaurantId: Int) : FavoriteResponse {
        return detailApi.deleteFavorite(restaurantId)
    }

    override suspend fun getEvaluationData(restaurantId: Int): EvaluationDataResponse {
        return detailApi.getEvaluationData(restaurantId)
    }

    override suspend fun postEvaluationData(
        restaurantId: Int,
        evaluationScore: RequestBody,
        evaluationSituations: List<MultipartBody.Part>,
        evaluationComment: RequestBody?,
        newImage: MultipartBody.Part?
    ): List<CommentDataResponse> {
        return detailApi.postEvaluationData(
            restaurantId,
            evaluationScore,
            evaluationSituations,
            evaluationComment,
            newImage
        )
    }

    override suspend fun deleteCommentData(restaurantId: Int, commentId: Int){
        return detailApi.deleteCommentData(restaurantId, commentId)
    }

    override suspend fun postCommentReport(restaurantId: Int, commentId: Int) {
        return detailApi.postCommentReport(restaurantId, commentId)
    }

    override suspend fun putEvalCommentReaction(evalCommentId: Int, reaction: String?) : EvalCommentReactionResponse {
        return detailApi.putEvalCommentReaction(evalCommentId, reaction)
    }

    override suspend fun putEvaluationReaction(
        evaluationId: Int,
        reaction: String?,
        userId: Int,
        role: String
    ) : EvaluationReactionResponse {
        return detailApi.putEvaluationReaction(evaluationId, reaction, userId, role)
    }
}