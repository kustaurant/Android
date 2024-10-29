package com.kust.kustaurant.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kust.kustaurant.MainActivity
import com.kust.kustaurant.R
import com.kust.kustaurant.data.getAccessToken

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 스플래시 화면 0.5초 후 이미 로그인한 전적이 있으면 home, 아니면 온보딩
        Handler(Looper.getMainLooper()).postDelayed({
            val preferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
            val isFirstLaunch = preferences.getBoolean("is_first_launch", false)
            Log.d("isFirstLaunch","${isFirstLaunch}")

            if (!isFirstLaunch) {
                val intent = Intent(this, OnboardingActivity::class.java)
                startActivity(intent)
            } else if (getAccessToken(this) == null) {
                val intent = Intent(this, StartActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 500) // 0.5초 동안 스플래시 화면을 보여줌
    }
}