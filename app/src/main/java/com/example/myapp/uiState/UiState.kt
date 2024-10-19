package com.example.myapp.uiState

import com.example.myapp.Data.Song

sealed class UiState {
    data class Success(val songs: List<Song>) : UiState()
    data class Error(val message: String) : UiState()
    object Loading : UiState()
}
