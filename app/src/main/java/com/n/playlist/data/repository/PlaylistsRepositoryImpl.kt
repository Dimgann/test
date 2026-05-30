package com.kotler.playlist.data.repository

import com.kotler.playlist.data.database.AppDatabase
import com.kotler.playlist.data.database.mapper.toDomain
import com.kotler.playlist.data.database.mapper.toDomainList
import com.kotler.playlist.domain.model.Playlist
import com.kotler.playlist.domain.repository.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val database: AppDatabase
) : PlaylistsRepository {
    
    private val playlistsDao = database.playlistsDao()
    private val tracksDao = database.tracksDao()
    
    override fun getAllPlaylists(): Flow<List<Playlist>> = 
        playlistsDao.getAllPlaylists().map { entities ->
            entities.map { e ->
                val tracks = tracksDao.getTracksByPlaylistId(e.id).valueOrNull()?.toDomainList() ?: emptyList()
                e.toDomain(tracks)
            }
        }
    
    override fun getPlaylistById(id: Long): Flow<Playlist?> = 
        playlistsDao.getPlaylistById(id).map { entity ->
            entity?.let { e ->
                val tracks = tracksDao.getTracksByPlaylistId(e.id).valueOrNull()?.toDomainList() ?: emptyList()
                e.toDomain(tracks)
            }
        }
    
    override suspend fun addNewPlaylist(name: String, description: String, coverImageUri: String?): Long = 
        playlistsDao.insertPlaylist(
            com.kotler.playlist.data.database.entity.PlaylistEntity(
                name = name, 
                description = description, 
                coverImageUri = coverImageUri
            )
        )
    
    override suspend fun deletePlaylistById(id: Long) {
        tracksDao.deleteTracksByPlaylistId(id)
        playlistsDao.deletePlaylist(id)
    }
    
    private suspend fun <T> Flow<T>.valueOrNull(): T? = 
        try { kotlinx.coroutines.flow.first() } catch (_: Exception) { null }
}