package com.kotler.playlist.data.repository

import com.kotler.playlist.data.preferences.SearchHistoryPreferences
import com.kotler.playlist.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchHistoryRepositoryImpl(
    private val preferences: SearchHistoryPreferences
) : SearchHistoryRepository {
    
    override suspend fun getHistory(): List<String> {
        return preferences.getEntries()
    }
    
    override fun addToHistory(word: String) {
        preferences.addEntry(word)
    }
    
    override fun clearHistory() {
        preferences.clearHistory()
    }
}