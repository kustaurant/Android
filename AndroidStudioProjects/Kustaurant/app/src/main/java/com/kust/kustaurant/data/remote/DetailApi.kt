package com.kust.kustaurant.data.remote

import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.data.model.DetailDataResponse
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
    ) : List<CommentDataResponse>
}