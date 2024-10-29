package com.kust.kustaurant.domain.usecase.mypage

import com.kust.kustaurant.data.model.MyPageResponse
import com.kust.kustaurant.domain.repository.MyPageRepository
import javax.inject.Inject

class GetMyPageDataUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): MyPageResponse {
        return myPageRepository.getMyPageData()
    }
}
