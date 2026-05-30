package com.kotler.playlist.domain.repository

import com.kotler.playlist.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    suspend fun searchTracks(expression: String): Result<List<Track>>
    fun getTrackByNameAndArtist(track: Track): Flow<Track?>
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun insertTrackToPlaylist(track: Track, playlistId: Long)
    suspend fun deleteTrackFromPlaylist(track: Track)
    suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean)
    fun getTracksByPlaylistId(playlistId: Long): Flow<List<Track>>
    suspend fun deleteTracksByPlaylistId(playlistId: Long)
}