package com.kust.kustaurant.data.di

import com.kust.kustaurant.data.repository.DetailRepositoryImpl
import com.kust.kustaurant.data.repository.DrawRepositoryImpl
import com.kust.kustaurant.data.repository.HomeRepositoryImpl
import com.kust.kustaurant.data.repository.NaverLoginRepositoryImpl
import com.kust.kustaurant.data.repository.TierRepositoryImpl
import com.kust.kustaurant.domain.repository.DetailRepository
import com.kust.kustaurant.domain.repository.DrawRepository
import com.kust.kustaurant.domain.repository.HomeRepository
import com.kust.kustaurant.domain.repository.NaverLoginRepository
import com.kust.kustaurant.domain.repository.TierRepository
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

    @Binds
    @Singleton
    abstract fun bindNaverLoginRepository(
        naverloginRepository: NaverLoginRepositoryImpl
    ): NaverLoginRepository
}
