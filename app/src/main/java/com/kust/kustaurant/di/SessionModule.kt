package com.kust.kustaurant.di

import com.kust.kustaurant.domain.common.session.SessionController
import com.kust.kustaurant.presentation.common.session.SessionControllerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SessionModule {
    @Binds
    @Singleton
    abstract fun bindSessionController(
        impl: SessionControllerImpl
    ): SessionController
}
