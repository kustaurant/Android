package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.domain.model.SearchRestaurant

interface SearchRepository {
    suspend fun getSearch(
        kw: String
    ):List<SearchRestaurant>
}