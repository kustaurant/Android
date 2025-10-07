package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.data.model.commnity.CommunityPostUploadImageResponse
import com.kust.kustaurant.domain.repository.CommunityRepository
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PostCommunityUploadImageUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(image : MultipartBody.Part) : CommunityPostUploadImageResponse {
        return communityRepository.postUploadImage(image)
    }
}