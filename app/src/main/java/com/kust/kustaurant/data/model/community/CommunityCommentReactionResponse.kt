package com.kust.kustaurant.data.model.community

data class CommunityCommentReactionResponse(
    val likeCount : Int,
    val dislikeCount : Int,
    val reactionType : String?,
)