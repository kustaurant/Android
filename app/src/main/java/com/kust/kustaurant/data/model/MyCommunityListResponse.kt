package com.kust.kustaurant.data.model

data class MyCommunityListResponse(
    val postId: Int,
    val postCategory: String,
    val postTitle: String,
    val postImgUrl: String?,
    val fullBody: String,
    val likeCount: Int,
    val commentCount: Int,
    val timeAgo: String,
    val body: String
)
