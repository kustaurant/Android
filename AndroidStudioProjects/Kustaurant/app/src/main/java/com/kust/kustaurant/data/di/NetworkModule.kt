package com.kust.kustaurant.data.di

import android.content.Context
import com.kust.kustaurant.data.remote.DetailApi
import com.kust.kustaurant.data.remote.HomeApi
import com.kust.kustaurant.data.remote.KustaurantApi
import com.kust.kustaurant.data.remote.LogoutApi
import com.kust.kustaurant.data.remote.NaverLoginApi
import com.kust.kustaurant.data.remote.MyPageApi
import com.kust.kustaurant.data.remote.NewAccessTokenApi
import com.kust.kustaurant.domain.usecase.login.PostNewAccessTokenDataUseCase
import com.kust.kustaurant.presentation.ui.mypage.NewAccessTokenViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return logging
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor, @ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(XAccessTokenInterceptor(context))  // context 전달로 수정
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://3.35.154.191:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMapApi(retrofit: Retrofit): KustaurantApi {
        return retrofit.create(KustaurantApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDetailApi(retrofit: Retrofit): DetailApi {
        return retrofit.create(DetailApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeApi(retrofit: Retrofit): HomeApi{
        return retrofit.create(HomeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNaverLoginApi(retrofit: Retrofit): NaverLoginApi {
        return retrofit.create(NaverLoginApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLogoutApi(retrofit: Retrofit): LogoutApi {
        return retrofit.create(LogoutApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewAccessTokenApi(retrofit: Retrofit): NewAccessTokenApi {
        return retrofit.create(NewAccessTokenApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMyPageApi(retrofit: Retrofit): MyPageApi{
        return retrofit.create(MyPageApi::class.java)
    }

}
