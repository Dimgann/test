package com.kotler.playlist.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val Context.searchHistoryDataStore: DataStore<Preferences> by preferencesDataStore(name = "search_history")

class SearchHistoryPreferences(private val context: Context) {
    private val dataStore = context.searchHistoryDataStore
    private val key = stringPreferencesKey("history_entries")
    private val scope = CoroutineScope(CoroutineName("history") + SupervisorJob())
    
    fun addEntry(word: String) {
        if (word.isBlank()) return
        scope.launch {
            dataStore.edit { prefs ->
                val current = prefs[key].orEmpty()
                val list = if (current.isNotEmpty()) current.split("|").toMutableList() else mutableListOf()
                list.remove(word)
                list.add(0, word)
                prefs[key] = list.take(10).joinToString("|")
            }
        }
    }
    
    suspend fun getEntries(): List<String> {
        return dataStore.data.first()[key]?.takeIf { it.isNotEmpty() }?.split("|") ?: emptyList()
    }
}