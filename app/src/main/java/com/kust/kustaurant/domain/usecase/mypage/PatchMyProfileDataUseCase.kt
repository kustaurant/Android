package com.kust.kustaurant.domain.usecase.mypage

import com.kust.kustaurant.data.model.MyProfileRequest
import com.kust.kustaurant.data.model.MyProfileResponse
import com.kust.kustaurant.domain.repository.MyPageRepository
import javax.inject.Inject

class PatchMyProfileDataUseCase @Inject constructor(
    private val myProfileRepository: MyPageRepository
) {
    suspend operator fun invoke(nickname : String, email : String, phoneNumber : String) {
        return myProfileRepository.patchProfileData(nickname, email, phoneNumber)
    }
}
