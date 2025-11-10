package com.kust.kustaurant.data.model.community

data class CommunityPostLikeResponse(
    val reactionType : String?,
    val likeCount : Int,
    val dislikeCount : Int,
    val netLikes : Int,
)
