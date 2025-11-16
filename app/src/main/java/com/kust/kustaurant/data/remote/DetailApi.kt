package com.kust.kustaurant.data.remote

import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.data.model.CommentReplyRequest
import com.kust.kustaurant.data.model.CommentReplyResponse
import com.kust.kustaurant.data.model.DetailDataResponse
import com.kust.kustaurant.data.model.EvalCommentReactionResponse
import com.kust.kustaurant.data.model.EvaluationDataResponse
import com.kust.kustaurant.data.model.EvaluationReactionResponse
import com.kust.kustaurant.data.model.FavoriteResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface DetailApi {
    @GET("/api/v2/restaurants/{restaurantId}")
    suspend fun getDetailData(
        @Path("restaurantId") restaurantId : Int
    ) : DetailDataResponse

    @GET("/api/v2/restaurants/{restaurantId}/comments")
    suspend fun getCommentData(
        @Path("restaurantId") restaurantId: Int,
        @Query("sort") sort: String
    ) : List<CommentDataResponse>

    @POST("/api/v2/auth/restaurants/{restaurantId}/comments/{evalCommentId}")
    suspend fun postCommentReplyData(
        @Path("restaurantId") restaurantId: Int,
        @Path("evalCommentId") evalCommentId: Int,
        @Body request: CommentReplyRequest
    ) : CommentReplyResponse

    @PUT("/api/v2/auth/restaurants/{restaurantId}/favorite")
    suspend fun putFavorite(
        @Path("restaurantId") restaurantId: Int
    ) : FavoriteResponse

    @DELETE("/api/v2/auth/restaurants/{restaurantId}/favorite")
    suspend fun deleteFavorite(
        @Path("restaurantId") restaurantId: Int
    ) : FavoriteResponse

    @GET("/api/v2/auth/restaurants/{restaurantId}/evaluation")
    suspend fun getEvaluationData(
        @Path("restaurantId") restaurantId: Int
    ) : EvaluationDataResponse

    @Multipart
    @POST("/api/v1/auth/restaurants/{restaurantId}/evaluation")
    suspend fun postEvaluationData(
        @Path("restaurantId") restaurantId: Int,
        @Part("evaluationScore") evaluationScore: RequestBody,
        @Part evaluationSituations: List<MultipartBody.Part>,
        @Part("evaluationComment") evaluationComment: RequestBody?,
        @Part newImage: MultipartBody.Part?
    ) : List<CommentDataResponse>

    @DELETE("/api/v1/auth/restaurants/{restaurantId}/comments/{commentId}")
    suspend fun deleteCommentData(
        @Path("restaurantId") restaurantId: Int,
        @Path("commentId") commentId: Int
    )

    @POST("api/v1/auth/restaurants/{restaurantId}/comments/{commentId}/report")
    suspend fun postCommentReport(
        @Path("restaurantId") restaurantId: Int,
        @Path("commentId") commentId: Int
    )

    @PUT("/api/v2/auth/eval-comments/{evalCommentId}")
    suspend fun putEvalCommentReaction(
        @Path("evalCommentId") evalCommentId: Int,
        @Query("reaction") reaction: String?
    ) : EvalCommentReactionResponse

    @PUT("/api/v2/auth/restaurants/evaluations/{evaluationId}/reaction")
    suspend fun putEvaluationReaction(
        @Path("evaluationId") evaluationId: Int,
        @Query("reaction") reaction: String?,
        @Query("id") userId: Int,
        @Query("role") role: String
    ) : EvaluationReactionResponse
}