package com.kust.kustaurant.domain.usecase.auth

import com.kust.kustaurant.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostLogoutDataUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend fun invoke():String{
        return authRepository.postLogout()
    }
}