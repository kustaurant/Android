package com.kust.kustaurant.data.remote

import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.data.model.CommentLikeResponse
import com.kust.kustaurant.data.model.DetailDataResponse
import com.kust.kustaurant.data.model.EvaluationDataRequest
import com.kust.kustaurant.data.model.EvaluationDataResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface DetailApi {
    @GET("/api/v1/auth/restaurants/{restaurantId}")
    suspend fun getDetailData(
        @Path("restaurantId") restaurantId : Int
    ) : DetailDataResponse

    @GET("/api/v1/restaurants/{restaurantId}")
    suspend fun getAnonDetailData(
        @Path("restaurantId") restaurantId : Int
    ) : DetailDataResponse

    @GET("/api/v1/restaurants/{restaurantId}/comments")
    suspend fun getCommentData(
        @Path("restaurantId") restaurantId: Int,
        @Query("sort") sort : String
    ) : List<CommentDataResponse>

    @POST("/api/v1/auth/restaurants/{restaurantId}/comments/{commentId}")
    suspend fun postCommentData(
        @Path("restaurantId") restaurantId: Int,
        @Path("commentId") commentId : Int,
        @Body inputText : String
    ) : CommentDataResponse

    @POST("/api/v1/auth/restaurants/{restaurantId}/favorite-toggle")
    suspend fun postFavoriteToggle(
        @Path("restaurantId") restaurantId: Int,
    ) : Boolean

    @GET("/api/v1/auth/restaurants/{restaurantId}/evaluation")
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

    @POST("/api/v1/auth/restaurants/{restaurantId}/comments/{commentId}/like")
    suspend fun postCommentLike(
        @Path("restaurantId") restaurantId: Int,
        @Path("commentId") commentId: Int
    ) : CommentLikeResponse

    @POST("/api/v1/auth/restaurants/{restaurantId}/comments/{commentId}/dislike")
    suspend fun postCommentDisLike(
        @Path("restaurantId") restaurantId: Int,
        @Path("commentId") commentId: Int
    ) : CommentLikeResponse
}