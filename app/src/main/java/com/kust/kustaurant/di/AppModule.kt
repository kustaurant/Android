package com.kust.kustaurant.di

import android.content.Context
import com.kust.kustaurant.presentation.ui.util.ImageUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideImageUtil(@ApplicationContext context: Context): ImageUtil {
        return ImageUtil(context)
    }
}
