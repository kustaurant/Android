package com.example.kustaurant.presentation.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailViewModel : ViewModel() {
    private val _tabTitles = MutableLiveData<List<String>>(listOf("메뉴","리뷰"))
    val tabTitles : LiveData<List<String>> = _tabTitles
}