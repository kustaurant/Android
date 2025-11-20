package com.kust.kustaurant.domain.usecase.auth

import com.kust.kustaurant.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteUserUseCase @Inject constructor(
    private val authRepository : AuthRepository
){
    suspend operator fun invoke(){
        return authRepository.deleteUser()
    }
}