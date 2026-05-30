package com.kotler.playlist.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotler.playlist.domain.model.Playlist
import com.kotler.playlist.domain.model.Track
import com.kotler.playlist.domain.repository.PlaylistsRepository
import com.kotler.playlist.domain.repository.TracksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistsRepository: PlaylistsRepository,
    private val tracksRepository: TracksRepository,
    playlistId: Long
) : ViewModel() {
    
    val playlist: StateFlow<Playlist?> = playlistsRepository
        .getPlaylistById(playlistId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    
    private val _playlistId = playlistId
    
    fun deleteCurrentPlaylist() {
        viewModelScope.launch {
            playlistsRepository.deletePlaylistById(_playlistId)
        }
    }

    fun addTrackToPlaylist(track: Track) {
        viewModelScope.launch {
            playlist.value?.id?.let { id ->
                tracksRepository.insertTrackToPlaylist(track, id)
            }
        }
    }
    
    fun removeTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            tracksRepository.deleteTrackFromPlaylist(track)
        }
    }
}