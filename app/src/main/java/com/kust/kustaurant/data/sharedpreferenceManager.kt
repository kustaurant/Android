package com.kust.kustaurant.data

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

fun saveAccessToken(context: Context, token: String) {
    val preferences = context.getSharedPreferences("app_preferences", AppCompatActivity.MODE_PRIVATE)
    val editor = preferences.edit()
    editor.putString("access_token", "Bearer "+token)
    editor.apply()
}

fun saveId(context: Context, userId: String) {
    val preferences = context.getSharedPreferences("app_preferences", AppCompatActivity.MODE_PRIVATE)
    val editor = preferences.edit()
    editor.putString("userId", userId)
    editor.apply()
}

fun getAccessToken(context: Context): String? {
    val preferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    return preferences.getString("access_token", null)
}

fun saveDeviceId(context: Context, deviceId: String) {
    val preferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    Log.d("SharedPrefManager", "Saving device ID: $deviceId")
    preferences.edit().putString("device_id", deviceId).apply()
}

fun getDeviceId(context: Context): String? {
    val preferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    return preferences.getString("device_id", null)
}

fun clearUserTokens(context: Context) {
    val prefs = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    prefs.edit()
        .remove("access_token")
        .remove("userId")
        .apply()
}