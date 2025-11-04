package com.kust.kustaurant.di.network

import com.kust.kustaurant.data.network.bus.ServiceDownBus
import com.kust.kustaurant.domain.common.notice.ServiceDownNotifier
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotifierBindingsModule {

    @Binds
    @Singleton
    abstract fun bindServiceDownNotifier(
        impl: ServiceDownBus
    ): ServiceDownNotifier
}