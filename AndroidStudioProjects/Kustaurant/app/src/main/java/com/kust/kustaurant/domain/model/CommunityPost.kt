package com.kust.kustaurant.domain.model

data class CommunityPost(
    val postTitle: String,
    val postBody: String,
    val status: String,
    val postCategory: String,
    val createdAt: String,
    val updatedAt: String?,
    val likeCount: Int,
    val user: CommunityUser,
)

data class CommunityUser(
    val userNickname : String,
    val rankImg : String
)
