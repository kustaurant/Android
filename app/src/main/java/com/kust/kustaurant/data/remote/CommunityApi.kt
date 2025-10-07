package com.kust.kustaurant.data.remote

import com.kust.kustaurant.data.model.commnity.CommunityPostCommentReactResponse
import com.kust.kustaurant.data.model.commnity.CommunityPostLikeResponse
import com.kust.kustaurant.data.model.commnity.CommunityPostScrapResponse
import com.kust.kustaurant.data.model.commnity.CommunityPostUploadImageResponse
import com.kust.kustaurant.data.model.commnity.PostCommentRequest
import com.kust.kustaurant.data.model.commnity.PostRequest
import com.kust.kustaurant.data.model.commnity.PostResponse
import com.kust.kustaurant.domain.model.community.AuthUserInfo
import com.kust.kustaurant.domain.model.community.CommunityPost
import com.kust.kustaurant.domain.model.community.CommunityPostComment
import com.kust.kustaurant.domain.model.community.CommunityRanking
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface CommunityApi {
    @GET("/api/v2/community/posts")
    suspend fun getCommunityPostListData(
        @Query("category") category: String,
        @Query("page") page: Int,
        @Query("sort") sort: String
    ): List<CommunityPost>

    @GET("/api/v2/community/ranking")
    suspend fun getCommunityRankingListData(
        @Query("sort") sort: String,
    ): List<CommunityRanking>

    @GET("/api/v2/community/{postId}")
    suspend fun getCommunityPostDetailData(
        @Path("postId") postId: Long,
    ): CommunityPost

    @POST("/api/v2/auth/community/{postId}/scraps")
    suspend fun postCommunityPostDetailScrap(
        @Path("postId") postId: Long,
        @Query("scrapped") scrapped : Boolean,
    ): CommunityPostScrapResponse

    @PUT("/api/v2/auth/community/{postId}/reaction")
    suspend fun postCommunityPostLikeToggle(
        @Path("postId") postId: Long,
        @Query("cmd") cmd : String?
    ): CommunityPostLikeResponse

    @POST("/api/v2/auth/community/posts/create")
    suspend fun postPostCreate(
        @Body postRequest : PostRequest
    ): CommunityPost

    @PATCH("/api/v2/auth/community/posts/{postId}")
    suspend fun patchModifyPost(
        @Path("postId") postId: String,
        @Body postRequest : PostRequest
    )

    @GET("/api/v2/auth/community/posts/{postId}")
    suspend fun getModifyPost(
        @Path("postId") postId: Long,
        @Query("user") user : AuthUserInfo,
    ) : PostResponse

    @DELETE("/api/v2/auth/community/{postId}")
    suspend fun deletePost(
        @Path("postId") postId: Long,
    )

    @DELETE("/api/v2/auth/comments/{commentId}")
    suspend fun deletePostComment(
        @Path("commentId") commentId: Long,
    )

    @Multipart
    @POST("/api/v2/auth/community/posts/image")
    suspend fun postCommunityUploadImage(
        @Part image: MultipartBody.Part
    ): CommunityPostUploadImageResponse

    @POST("/api/v2/auth/posts/{postId}/comments")
    suspend fun postCommunityPostCommentReply(
        @Query("postId") postId: Long,
        @Body postCommentRequest : PostCommentRequest
    ): List<CommunityPostComment>

    @PUT("/api/v2/auth/community/comments/{commentId}/reaction")
    suspend fun putCommentLikeToggle(
        @Path("commentId") commentId: Long,
        @Query("reaction") reaction: String?
    ): CommunityPostCommentReactResponse
}