package com.kust.kustaurant.domain.model.community

data class CommunityPostListItem(
    val postId: Long,
    val category: String,
    val title: String,
    val body: String,

    val writeruserId: Long,
    val writernickname: String,
    val writerevalCount: Long,
    val writericonUrl: String?,

    val photoUrl: String?,
    val timeAgo: String?,

    val totalLikes: Long,
    val commentCount: Long,
)
