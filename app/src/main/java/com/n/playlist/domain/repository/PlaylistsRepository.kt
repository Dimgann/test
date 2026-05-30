package com.kotler.playlist.domain.repository

import com.kotler.playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getAllPlaylists(): Flow<List<Playlist>>
    fun getPlaylistById(playlistId: Long): Flow<Playlist?>
    suspend fun addNewPlaylist(name: String, description: String, coverImageUri: String? = null): Long
    suspend fun deletePlaylistById(playlistId: Long)
    suspend fun addTrackToPlaylist(playlistId: Long, track: Track)
}