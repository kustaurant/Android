package com.example.kustaurant.data.di

import com.example.kustaurant.data.repository.TierRepositoryImpl
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
}
