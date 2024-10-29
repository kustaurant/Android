package com.kust.kustaurant.domain.model


data class CommunityPost(
    val postId: Int,
    val postTitle: String,
    val postBody: String,
    val status: String,
    val postCategory: String,
    val createdAt: String,
    val updatedAt: String?,
    val likeCount: Int,
    val user: User,
    val postCommentList: List<CommunityPostComment>?,
    val timeAgo : String,
    val commentCount : Int,
    val postPhotoImgUrl : String?,
    val postVisitCount : String,
    val postScrapList : List<ScrapUser>?,
    val scrapCount: Int,
    val likeUserList : List<ScrapUser>?,
    val isScraped : Boolean,
    val isLiked : Boolean,
    val isPostMine : Boolean
)

data class User(
    val userNickname: String,
    val rankImg: String,
    val evaluationCount: Int,
    val rank: String?
)

data class CommunityPostComment(
    val commentId: Int,
    val commentBody: String,
    val status : String,
    val user: User,
    val likeCount: Int,
    val dislikeCount: Int,
    val createdAt : String,
    val updatedAt : String?,
    val repliesList : List<CommunityPostComment>,
    val timeAgo : String,
    val isDisliked : Boolean,
    val isLiked : Boolean,
    val isCommentMine : Boolean,
)

data class ScrapUser(
    val userId: Int,
    val providerId: String,
    //val accessToken: String,
    //val refreshToken: String,
    //val userPassword: String?,
    val userEmail: String,
    val phoneNumber: String,
    val userNickname: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val loginApi: String,
    //val userRole: String,
    //val roleKey: String,
    val rankImg: String
)


