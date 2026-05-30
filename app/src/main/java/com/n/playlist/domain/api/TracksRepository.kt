package com.kotler.playlist.domain.api

import com.kotler.playlist.data.network.Track

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>
}