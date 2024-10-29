package com.kust.kustaurant.domain.usecase.mypage

import com.kust.kustaurant.data.model.MyCommentResponse
import com.kust.kustaurant.data.model.MyFavoriteResponse
import com.kust.kustaurant.domain.repository.MyPageRepository
import javax.inject.Inject

class GetMyCommunityCommentUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): List<MyCommentResponse> {
        return myPageRepository.getCommunityCommentData()
    }
}
