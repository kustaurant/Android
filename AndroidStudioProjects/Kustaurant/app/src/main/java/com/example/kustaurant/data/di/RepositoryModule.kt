package com.example.kustaurant.data.di

import com.example.kustaurant.data.remote.DetailApi
import com.example.kustaurant.data.repository.DetailRepositoryImpl
import com.example.kustaurant.data.repository.TierRepositoryImpl
import com.example.kustaurant.domain.repository.DetailRepository
import com.example.kustaurant.domain.repository.TierRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
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
}
