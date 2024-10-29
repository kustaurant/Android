package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostCommunityPostDetailCommentLike @Inject constructor(
    private val communityRepository: CommunityRepository
) {
//    suspend operator fun invoke(sort: String): List<CommunityRanking> {
//        //return communityRepository.getCommunityRankingListData(sort)
//    }

    /*
    viewModelScope.launch {
            try {
                patchMyProfileDataUseCase(nickname, email, phoneNumber)
                _toastMessage.postValue("프로필이 업데이트 되었습니다.")
                _updateSuccess.postValue(true)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                handleErrorResponse(errorBody)
            } catch (e: Exception) {
                _toastMessage.postValue("네트워크 에러가 발생했습니다.")
            }
        }
     */
}