package com.kust.kustaurant.domain.usecase.login

import com.kust.kustaurant.data.model.LoginResponse
import com.kust.kustaurant.domain.repository.NewAccessTokenRepository
import javax.inject.Inject

class PostNewAccessTokenDataUseCase @Inject constructor(
    private val newaccesstokenRepository: NewAccessTokenRepository
) {
    suspend fun invoke():LoginResponse{
        return newaccesstokenRepository.postNewAccessToken()
    }
}