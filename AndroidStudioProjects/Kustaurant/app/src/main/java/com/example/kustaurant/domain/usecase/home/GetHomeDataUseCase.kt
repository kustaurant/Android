package com.example.kustaurant.domain.usecase.home

import com.example.kustaurant.data.model.HomeResponse
import com.example.kustaurant.domain.repository.HomeRepository
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