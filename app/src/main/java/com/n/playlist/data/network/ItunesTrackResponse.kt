package com.kotler.playlist.network.model

import com.google.gson.annotations.SerializedName
import com.kotler.playlist.domain.model.Track

data class ItunesTrackResponse(
    @SerializedName("resultCount") val resultCount: Int?,
    @SerializedName("results") val results: List<ItunesTrackItem>?
)

data class ItunesTrackItem(
    @SerializedName("trackId") val trackId: Long?,
    @SerializedName("trackName") val trackName: String?,
    @SerializedName("artistName") val artistName: String?,
    @SerializedName("trackTimeMillis") val trackTimeMillis: Long?,
    @SerializedName("artworkUrl100") val artworkUrl100: String?
)

fun ItunesTrackItem.toDomain(): Track {
    return Track(
        id = trackId ?: 0,
        trackName = trackName ?: "Неизвестный трек",
        artistName = artistName ?: "Неизвестный исполнитель",
        trackTime = formatTime(trackTimeMillis),
        image = artworkUrl100 ?: "",
        favorite = false,
        playlistId = 0
    )
}

private fun formatTime(millis: Long?): String {
    if (millis == null || millis <= 0) return "0:00"
    val sec = millis / 1000
    return "%d:%02d".format(sec / 60, sec % 60)
}