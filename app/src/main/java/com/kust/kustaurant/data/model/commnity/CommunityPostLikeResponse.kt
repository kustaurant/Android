package com.kust.kustaurant.data.model.commnity

data class CommunityPostLikeResponse(
    val reactionType : String?,
    val likeCount : Int,
    val dislikeCount : Int,
    val netLikes : Int,
)
