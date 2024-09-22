package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostCommunityPostDetailLike @Inject constructor(
    private val communityRepository: CommunityRepository
) {
}