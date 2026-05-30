package com.kotler.playlist.data.local

import com.kotler.playlist.domain.model.Playlist
import com.kotler.playlist.domain.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class DatabaseMock(private val scope: CoroutineScope) {
    
    private val playlists = mutableListOf<Playlist>()
    private val tracks = mutableListOf<Track>()
    
    fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        delay(300)
        val result = playlists.map { playlist ->
            val playlistTracks = tracks.filter { it.playlistId == playlist.id }
            playlist.copy(tracks = playlistTracks)
        }
        emit(result)
    }
    
    fun getPlaylist(id: Long): Flow<Playlist?> = flow {
        delay(200)
        val playlist = playlists.find { it.id == id }
        if (playlist != null) {
            val playlistTracks = tracks.filter { it.playlistId == playlist.id }
            emit(playlist.copy(tracks = playlistTracks))
        } else {
            emit(null)
        }
    }
    
    fun addNewPlaylist(name: String, description: String) {
        val newId = (playlists.maxOfOrNull { it.id } ?: 0) + 1
        playlists.add(
            Playlist(
                id = newId,
                name = name,
                description = description,
                tracks = emptyList()
            )
        )
    }
    
    fun deletePlaylistById(playlistId: Long) {
        playlists.removeIf { it.id == playlistId }
        tracks.removeIf { it.playlistId == playlistId }
    }
    
    fun addTrackToPlaylist(playlistId: Long, track: Track) {
        tracks.removeIf { it.id == track.id && it.playlistId == playlistId }
        tracks.add(track.copy(playlistId = playlistId))
    }
    
    fun removeTrackFromPlaylist(trackId: Long) {
        val track = tracks.find { it.id == trackId }
        track?.let { tracks.add(it.copy(playlistId = 0)) }
    }
    
    fun getFavoriteTracks(): Flow<List<Track>> = flow {
        delay(200)
        emit(tracks.filter { it.favorite })
    }
    
    fun updateTrackFavorite(trackId: Long, isFavorite: Boolean) {
        val index = tracks.indexOfFirst { it.id == trackId }
        if (index != -1) {
            tracks[index] = tracks[index].copy(favorite = isFavorite)
        }
    }
    
    fun getTrackByNameAndArtist(track: Track): Flow<Track?> = flow {
        emit(tracks.find { 
            it.trackName.equals(track.trackName, ignoreCase = true) && 
            it.artistName.equals(track.artistName, ignoreCase = true) 
        })
    }
}