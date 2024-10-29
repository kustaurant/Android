package com.kust.kustaurant.domain.usecase.login

import com.kust.kustaurant.domain.repository.LogoutRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostLogoutDataUseCase @Inject constructor(
    private val logoutRepository: LogoutRepository
){
    suspend fun invoke():String{
        return logoutRepository.postLogout()
    }
}