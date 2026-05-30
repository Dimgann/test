package com.kotler.playlist.data.database.mapper

import com.kotler.playlist.data.database.entity.PlaylistEntity
import com.kotler.playlist.domain.model.Playlist
import com.kotler.playlist.domain.model.Track

fun PlaylistEntity.toDomain(tracks: List<Track>) = Playlist(
    id = id,
    name = name,
    description = description,
    coverImageUri = coverImageUri,
    tracks = tracks
)

fun Playlist.toEntity() = PlaylistEntity(
    id = id,
    name = name,
    description = description,
    coverImageUri = coverImageUri
)