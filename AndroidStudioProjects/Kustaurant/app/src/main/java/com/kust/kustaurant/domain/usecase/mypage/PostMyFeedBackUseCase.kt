package com.kust.kustaurant.domain.usecase.mypage

import com.kust.kustaurant.domain.repository.MyPageRepository
import javax.inject.Inject

class PostMyFeedBackUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(feedBack : String) {
        return myPageRepository.postFeedBackData(feedBack)
    }
}
