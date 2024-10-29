package com.kust.kustaurant.domain.usecase.home

import com.kust.kustaurant.data.model.HomeResponse
import com.kust.kustaurant.domain.repository.HomeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetHomeDataUseCase @Inject constructor(
    private val homeRepository: HomeRepository
){
    suspend fun invoke():HomeResponse{
        return homeRepository.getHome()
    }
}