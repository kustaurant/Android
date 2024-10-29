package com.kust.kustaurant.domain.usecase.mypage

import com.kust.kustaurant.data.model.MyPageResponse
import com.kust.kustaurant.data.model.MyProfileResponse
import com.kust.kustaurant.domain.repository.MyPageRepository
import javax.inject.Inject

class GetMyProfileDataUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): MyProfileResponse {
        return myPageRepository.getProfileData()
    }
}
