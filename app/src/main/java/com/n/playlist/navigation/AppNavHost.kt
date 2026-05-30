package com.kotler.playlist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kotler.playlist.ui.favorites.FavoritesScreen
import com.kotler.playlist.ui.favorites.FavoritesViewModel
import com.kotler.playlist.ui.main.MainScreen
import com.kotler.playlist.ui.playlist.PlaylistScreen
import com.kotler.playlist.ui.playlist.PlaylistViewModel
import com.kotler.playlist.ui.playlists.PlaylistsScreen
import com.kotler.playlist.ui.playlists.PlaylistsViewModel
import com.kotler.playlist.ui.search.SearchScreen
import com.kotler.playlist.ui.search.SearchViewModel
import com.kotler.playlist.ui.shared.SharedTrackViewModel
import com.kotler.playlist.ui.trackdetails.TrackDetailsScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    val searchVM: SearchViewModel = koinViewModel()
    val playlistsVM: PlaylistsViewModel = koinViewModel()
    val sharedTrackVM: SharedTrackViewModel = koinViewModel()
    val favoritesVM: FavoritesViewModel = koinViewModel()
    val track by sharedTrackVM.track.collectAsState()

    NavHost(navController, Destination.Main.route, modifier) {
        composable(Destination.Main.route) { MainScreen({ navController.navigate(Destination.Search.route) }, { navController.navigate(Destination.Playlists.route) }, { navController.navigate(Destination.Favorites.route) }) }
        composable(Destination.Search.route) { SearchScreen(searchVM, { navController.popBackStack() }, { id -> 
            track?.let { t -> if (t.id == id) { /* already in VM */ } } 
            // For simplicity in this structure, we assume search results are cached or re-fetched. 
            // In production, pass ID and fetch. Here we navigate directly:
            navController.navigate(Destination.TrackDetails.route) 
        }) }
        composable(Destination.Playlists.route) { PlaylistsScreen(playlistsVM, { navController.navigate("new_playlist") }, { id -> navController.navigate(Destination.Playlist.create(id)) }, { navController.popBackStack() }) }
        composable(Destination.Playlist.route, arguments = listOf(navArgument("id") { type = NavType.LongType })) { back ->
            val id = back.arguments?.getLong("id") ?: 0L
            val playlistVM: PlaylistViewModel = koinViewModel { org.koin.core.parameter.parametersOf(id) }
            PlaylistScreen(playlistVM, { navController.popBackStack() })
        }
        composable(Destination.Favorites.route) { FavoritesScreen(favoritesVM, { navController.popBackStack() }, { id -> navController.navigate(Destination.TrackDetails.route) }) }
        composable(Destination.TrackDetails.route) {
            track?.let { t -> TrackDetailsScreen(t, t.favorite, { navController.popBackStack() }, { favoritesVM.toggle(t.id, !t.favorite) }, { /* TODO: open bottom sheet */ }) }
        }
        composable(Destination.Playlists.route) {
            val viewModel: PlaylistsViewModel = koinViewModel()
            PlaylistsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToNewPlaylist = { navController.navigate(Destination.NewPlaylist.route) },
                onNavigateToPlaylist = { id -> 
                    navController.navigate(Destination.Playlist.createRoute(id)) 
                }
            )
        }
        composable(
            route = Destination.Playlist.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: return@composable
            val viewModel: PlaylistViewModel = koinViewModel { parametersOf(id) }
            
            PlaylistScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onTrackClick = { index -> 
                    // TODO: навигация на детали трека, если нужно
                }
            )
        }
    }
}