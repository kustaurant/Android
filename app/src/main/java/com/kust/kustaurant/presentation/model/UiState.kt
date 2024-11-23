package com.kust.kustaurant.presentation.model

sealed class UiState {
    data object Idle : UiState()
    data object Loading : UiState()
    data class Success<T>(val data: T) : UiState()
    sealed class Error : UiState() {
        data object Forbidden : Error() // Err code : 403
        data class Other(val message: String) : Error()
    }
}