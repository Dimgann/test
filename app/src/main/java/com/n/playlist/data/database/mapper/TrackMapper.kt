package com.kotler.playlist.data.database.mapper

import com.kotler.playlist.data.database.entity.TrackEntity
import com.kotler.playlist.domain.model.Track

fun TrackEntity.toDomain(): Track {
    return Track(
        id = id,
        trackName = trackName,
        artistName = artistName,
        trackTime = trackTime,
        image = image,
        favorite = favorite,
        playlistId = playlistId
    )
}

fun Track.toEntity(): TrackEntity {
    return TrackEntity(
        id = id,
        trackName = trackName,
        artistName = artistName,
        trackTime = trackTime,
        image = image,
        favorite = favorite,
        playlistId = playlistId
    )
}

fun List<TrackEntity>.toDomainList(): List<Track> = map { it.toDomain() }