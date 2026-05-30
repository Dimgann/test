package com.kotler.playlist.ui.search

import com.kotler.playlist.domain.model.Track

sealed class SearchState {
    object Initial : SearchState()
    object Loading : SearchState()
    data class Success(val tracks: List<Track>) : SearchState()
    data class Error(val message: String, val query: String) : SearchState()
    object Empty : SearchState()
}