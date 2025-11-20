package com.kust.kustaurant.di

import android.content.Context
import com.kust.kustaurant.domain.common.appEvent.AppEvents
import com.kust.kustaurant.presentation.common.appEvent.AppEventsImpl
import com.kust.kustaurant.presentation.ui.util.ImageUtil
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class  AppModule {
    @Binds
    @Singleton
    abstract fun bindAppEvents(impl: AppEventsImpl): AppEvents

    companion object {
        @Provides
        fun provideImageUtil(@ApplicationContext context: Context): ImageUtil =
            ImageUtil(context)
    }
}
