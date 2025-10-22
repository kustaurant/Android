package com.kust.kustaurant.domain.model.community

data class CommunityRanking(
    val userId : Long,
    val nickname : String,
    val iconUrl : String,
    val evaluationCount: Int,
    val rank: Int,
)