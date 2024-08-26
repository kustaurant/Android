package com.kust.kustaurant.domain.usecase.mypage

import com.kust.kustaurant.data.model.MyCommunityListResponse
import com.kust.kustaurant.data.model.MyEvaluateResponse
import com.kust.kustaurant.domain.repository.MyPageRepository
import javax.inject.Inject

class GetMyEvaluateDataUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): List<MyEvaluateResponse> {
        return myPageRepository.getEvaluateData()
    }
}
