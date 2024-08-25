package com.kust.kustaurant.data.remote

import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.data.model.DetailDataResponse
import com.kust.kustaurant.data.model.EvaluationDataRequest
import com.kust.kustaurant.data.model.EvaluationDataResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DetailApi {
    @GET("/api/v1/restaurants/{restaurantId}")
    suspend fun getDetailData(
        @Path("restaurantId") restaurantId : Int
    ) : DetailDataResponse

    @GET("/api/v1/restaurants/{restaurantId}/comments")
    suspend fun getCommentData(
        @Path("restaurantId") restaurantId: Int,
        @Query("sort") sort : String
    ) : List<CommentDataResponse>

    @POST("/api/v1/restaurants/{restaurantId}/comments/{commentId}")
    suspend fun postCommentData(
        @Path("restaurantId") restaurantId: Int,
        @Path("commentId") commentId : Int,
        @Body inputText : String
    ) : CommentDataResponse

    @POST("/api/v1/restaurants/{restaurantId}/favorite-toggle")
    suspend fun postFavoriteToggle(
        @Path("restaurantId") restaurantId: Int,
    ) : Boolean

    @GET("/api/v1/restaurants/{restaurantId}/evaluation")
    suspend fun getEvaluationData(
        @Path("restaurantId") restaurantId: Int
    ) : EvaluationDataResponse

    @POST("/api/v1/restaurants/{restaurantId}/evaluation")
    suspend fun postEvaluationData(
        @Path("restaurantId") restaurantId: Int,
        @Body request : EvaluationDataRequest
    ) : DetailDataResponse
}