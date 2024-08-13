package com.example.kustaurant.data.di

import com.example.kustaurant.data.repository.DetailRepositoryImpl
import com.example.kustaurant.data.repository.DrawRepositoryImpl
import com.example.kustaurant.data.repository.HomeRepositoryImpl
import com.example.kustaurant.data.repository.TierRepositoryImpl
import com.example.kustaurant.domain.repository.DetailRepository
import com.example.kustaurant.domain.repository.DrawRepository
import com.example.kustaurant.domain.repository.HomeRepository
import com.example.kustaurant.domain.repository.TierRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMapRepository(
        tierRepositoryImpl: TierRepositoryImpl
    ): TierRepository

    @Binds
    @Singleton
    abstract fun bindDetailRepository(
        detailRepositoryImpl: DetailRepositoryImpl
    ) : DetailRepository

    @Binds
    @Singleton
    abstract fun bindDrawRepository(
        drawRepositoryImpl: DrawRepositoryImpl
    ): DrawRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        homeRepository: HomeRepositoryImpl
    ): HomeRepository
}
