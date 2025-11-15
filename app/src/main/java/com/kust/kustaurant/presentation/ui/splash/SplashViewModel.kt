package com.kust.kustaurant.presentation.ui.splash

import androidx.lifecycle.ViewModel
import com.kust.kustaurant.data.datasource.AuthPreferenceDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val prefs: AuthPreferenceDataSource,
): ViewModel() {
    fun hasLoginInfo(): Boolean {
        prefs.getAccessToken()?.let {
            return true
        }
        return false
    }
}