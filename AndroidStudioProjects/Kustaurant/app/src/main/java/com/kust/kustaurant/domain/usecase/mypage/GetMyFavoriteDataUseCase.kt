package com.kust.kustaurant.domain.usecase.mypage

import com.kust.kustaurant.data.model.MyFavoriteResponse
import com.kust.kustaurant.data.model.MyPageResponse
import com.kust.kustaurant.domain.repository.MyPageRepository
import javax.inject.Inject

class GetMyFavoriteDataUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): List<MyFavoriteResponse> {
        return myPageRepository.getFavoriteData()
    }
}
