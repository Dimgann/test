package com.kotler.playlist.domain.repository

interface SearchHistoryRepository {
    suspend fun getHistory(): List<String>
    fun addToHistory(word: String)
    fun clearHistory()
}