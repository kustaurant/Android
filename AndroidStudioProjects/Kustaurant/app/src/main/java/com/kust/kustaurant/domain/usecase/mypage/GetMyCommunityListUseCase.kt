package com.kust.kustaurant.domain.usecase.mypage

import com.kust.kustaurant.data.model.MyCommunityListResponse
import com.kust.kustaurant.domain.repository.MyPageRepository
import javax.inject.Inject

class GetMyCommunityListUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): List<MyCommunityListResponse> {
        return myPageRepository.getCommunityListData()
    }
}
