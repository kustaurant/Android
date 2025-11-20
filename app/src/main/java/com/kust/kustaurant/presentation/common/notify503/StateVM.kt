package com.kust.kustaurant.presentation.common.notify503

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StateVM @Inject constructor() : ViewModel() {
    private var lastHandledSeq: Long = 0L
    fun shouldHandle(seq: Long): Boolean {
        if (seq == lastHandledSeq) return false
        lastHandledSeq = seq
        return true
    }
}
