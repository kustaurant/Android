package com.kust.kustaurant.data.remote

import com.kust.kustaurant.data.model.CommunityPostCommentReactResponse
import com.kust.kustaurant.data.model.CommunityPostScrapResponse
import com.kust.kustaurant.data.model.CommunityPostLikeResponse
import com.kust.kustaurant.domain.model.CommunityPost
import com.kust.kustaurant.domain.model.CommunityPostComment
import com.kust.kustaurant.domain.model.CommunityRanking
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommunityApi {
    @GET("/api/v1/community/posts")
    suspend fun getCommunityPostListData(
        @Query("postCategory") postCategory: String,
        @Query("page") page: Int,
        @Query("sort") sort: String
    ): List<CommunityPost>

    @GET("/api/v1/community/ranking")
    suspend fun getCommunityRankingListData(
        @Query("sort") sort: String,
    ): List<CommunityRanking>

    @GET("/api/v1/community/{postId}")
    suspend fun getCommunityPostDetailData(
        @Path("postId") postId: Int,
    ): CommunityPost

    @POST("/api/v1/auth/community/{postId}/scraps")
    suspend fun postCommunityPostDetailScrap(
        @Path("postId") postId: Int,
    ): CommunityPostScrapResponse

    @POST("/api/v1/auth/community/{postId}/likes")
    suspend fun postCommunityPostDetailLike(
        @Path("postId") postId: Int,
    ): CommunityPostLikeResponse

    @DELETE("/api/v1/auth/community/{postId}")
    suspend fun deletePost(
        @Path("postId") postId: Int,
    )

    @DELETE("/api/v1/auth/community/comment/{commentId}")
    suspend fun deletePostComment(
        @Path("commentId") commentId: Int,
    )

    @POST("/api/v1/auth/community/comments")
    suspend fun postCommunityPostCommentReply(
        @Query("content") content: String,
        @Query("postId") postId: String,
        @Query("parentCommentId") parentCommentId: String? = null,
    ): CommunityPostComment

    @POST("/api/v1/auth/community/comments/{commentId}/{action}")
    suspend fun postCommentReact(
        @Path("commentId") commentId: Int,
        @Path("action") action: String,
    ): CommunityPostCommentReactResponse


}