package com.kust.kustaurant.di.network

import com.kust.kustaurant.data.datasource.AuthPreferenceDataSource
import com.kust.kustaurant.data.local.AuthPreferenceDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindAuthPreferenceDataSource(
        impl: AuthPreferenceDataSourceImpl
    ): AuthPreferenceDataSource
}