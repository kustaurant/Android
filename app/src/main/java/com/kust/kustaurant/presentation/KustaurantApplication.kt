package com.kust.kustaurant.presentation

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KustaurantApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        // application 전역에 다크모드 방지를 선언하였습니다.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}