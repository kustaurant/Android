package com.kust.kustaurant.domain.usecase.home

import com.kust.kustaurant.domain.model.SearchRestaurant
import com.kust.kustaurant.domain.repository.SearchRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSearchDataUseCase @Inject constructor(
    private val searchRepository: SearchRepository
){
    suspend fun invoke(kw: String):List<SearchRestaurant>{
        return searchRepository.getSearch(kw)
    }
}