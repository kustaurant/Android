package com.kust.kustaurant.domain.usecase.login

import com.kust.kustaurant.domain.repository.GoodByeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteUserUseCase @Inject constructor(
    private val goodByeRepository: GoodByeRepository
) {
    suspend operator fun invoke(): String {
        return goodByeRepository.deleteUser()
    }
}

