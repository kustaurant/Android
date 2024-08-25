package com.kust.kustaurant.domain.usecase.login

import com.kust.kustaurant.domain.repository.NewAccessTokenRepository
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class PostNewAccessTokenDataUseCase @Inject constructor(
    private val newaccesstokenRepository: NewAccessTokenRepository
) {
    suspend fun invoke():Response<ResponseBody>{
        return newaccesstokenRepository.postNewAccessToken()
    }
}