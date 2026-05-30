package com.kotler.playlist.navigation

sealed class Destination(val route: String) {
    object Main : Destination("main")
    object Search : Destination("search")
    object AllPlaylists : Destination("playlists")
    object Playlist : Destination("playlist/{index}") {
        fun createRoute(index: Int) = "playlist/$index"
    }
    object NewPlaylist : Destination("new_playlist")
    object Favorites : Destination("favorites")
    object Settings : Destination("settings")
    object TrackDetails : Destination("track_details/{trackId}") {
        fun createRoute(trackId: Long) = "track_details/$trackId"
    }
}