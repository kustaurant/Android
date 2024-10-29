package com.kust.kustaurant.domain.model

data class CommunityRanking(
    val userNickname: String,
    val rankImg: String,
    val evaluationCount: Int,
    val rank: Int,
)