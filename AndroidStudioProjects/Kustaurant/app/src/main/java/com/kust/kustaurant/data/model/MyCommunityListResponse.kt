package com.kust.kustaurant.data.model

data class MyCommunityListResponse(
    val postCategory: String,
    val postTitle: String,
    val postBody: String,
    val likeCount: Int,
    val commentCount: Int,
    val timeAgo: String
)
