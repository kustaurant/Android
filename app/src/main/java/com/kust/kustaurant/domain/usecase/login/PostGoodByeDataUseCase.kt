package com.kust.kustaurant.domain.usecase.login

import com.kust.kustaurant.domain.repository.GoodByeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostGoodByeDataUseCase @Inject constructor(
    private val goodByeRepository: GoodByeRepository
){
    suspend fun invoke():String{
        return goodByeRepository.postGoodBye()
    }
}