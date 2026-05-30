package com.kotler.playlist.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotler.playlist.domain.model.Track
import com.kotler.playlist.domain.repository.SearchHistoryRepository
import com.kotler.playlist.domain.repository.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val tracksRepository: TracksRepository,
    private val historyRepository: SearchHistoryRepository
) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    private val _searchState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchState = _searchState.asStateFlow()
    
    private var lastQuery: String? = null
    
    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(800)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotBlank()) {
                        lastQuery = query
                        performSearch(query)
                        historyRepository.addToHistory(query.trim())
                    } else {
                        _searchState.update { SearchState.Initial }
                    }
                }
        }
    }
    
    fun updateQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun clearSearch() {
        _searchQuery.value = ""
        _searchState.update { SearchState.Initial }
    }
    
    fun retrySearch() {
        lastQuery?.let { performSearch(it) }
    }
    
    suspend fun getHistory(): List<String> = historyRepository.getHistory()
    
    private fun performSearch(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchState.update { SearchState.Loading }
            
            tracksRepository.searchTracks(query)
                .onSuccess { tracks ->
                    _searchState.update { 
                        if (tracks.isEmpty()) SearchState.Empty else SearchState.Success(tracks) 
                    }
                }
                .onFailure { error ->
                    _searchState.update { SearchState.Error(error.message ?: "Ошибка", query) }
                }
        }
    }
}

sealed class SearchState {
    object Initial : SearchState()
    object Loading : SearchState()
    data class Success(val tracks: List<Track>) : SearchState()
    data class Error(val message: String, val query: String) : SearchState()
    object Empty : SearchState()
}