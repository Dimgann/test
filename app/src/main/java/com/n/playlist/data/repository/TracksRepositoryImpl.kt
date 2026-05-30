package com.kotler.playlist.data.repository

import com.kotler.playlist.data.database.AppDatabase
import com.kotler.playlist.data.database.mapper.toDomain
import com.kotler.playlist.data.database.mapper.toEntity
import com.kotler.playlist.data.database.mapper.toDomainList
import com.kotler.playlist.domain.model.Track
import com.kotler.playlist.domain.repository.TracksRepository
import com.kotler.playlist.network.RetrofitClient
import com.kotler.playlist.network.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

class TracksRepositoryImpl(
    private val database: AppDatabase
) : TracksRepository {
    
    private val dao = database.tracksDao()
    
    override suspend fun searchTracks(expression: String): Result<List<Track>> = withContext(Dispatchers.IO) {
        try {
            if (expression.isBlank()) return@withContext Result.success(emptyList())
            
            val response = RetrofitClient.itunesApi.searchTracks(query = expression.trim())
            
            if (response.resultCount == null || response.resultCount == 0) {
                return@withContext Result.success(emptyList())
            }
            
            val tracks = response.results
                ?.filter { it?.trackName != null && it.artistName != null }
                ?.mapNotNull { it?.toDomain() }
                ?: emptyList()
            
            Result.success(tracks)
            
        } catch (e: IOException) {
            Result.failure(IOException("Нет соединения"))
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка: ${e.message}"))
        }
    }
    
    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return dao.getTrackByNameAndArtist(track.trackName, track.artistName)
            .map { it?.toDomain() }
    }
    
    override fun getFavoriteTracks(): Flow<List<Track>> {
        return dao.getFavoriteTracks().map { it.toDomainList() }
    }
    
    override fun getTracksByPlaylistId(playlistId: Long): Flow<List<Track>> {
        return dao.getTracksByPlaylistId(playlistId).map { it.toDomainList() }
    }
    
    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        dao.insertTrack(track.copy(playlistId = playlistId).toEntity())
    }
    
    override suspend fun deleteTrackFromPlaylist(track: Track) {
        dao.updatePlaylistId(track.id, 0)
    }
    
    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        dao.updateFavoriteStatus(track.id, isFavorite)
    }
    
    override suspend fun deleteTracksByPlaylistId(playlistId: Long) {
        dao.deleteTracksByPlaylistId(playlistId)
    }
}