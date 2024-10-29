package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.remote.SearchApi
import com.kust.kustaurant.domain.model.SearchRestaurant
import com.kust.kustaurant.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi
):SearchRepository{
    override suspend fun getSearch(kw: String): List<SearchRestaurant> {
        return searchApi.getSearch(kw)
    }
}