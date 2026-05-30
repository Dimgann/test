package com.kotler.playlist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kotler.playlist.ui.main.MainScreen
import com.kotler.playlist.ui.playlist.PlaylistViewModel
import com.kotler.playlist.ui.playlist.PlaylistScreen
import com.kotler.playlist.ui.playlists.PlaylistsScreen
import com.kotler.playlist.ui.playlists.PlaylistsViewModel
import com.kotler.playlist.ui.search.SearchScreen
import com.kotler.playlist.ui.search.SearchViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PlaylistNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel,
    playlistsViewModel: PlaylistsViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Destination.Main.route,
        modifier = modifier
    ) {
        composable(Destination.Main.route) {
            MainScreen(
                onNavigateToSearch = { navController.navigate(Destination.Search.route) },
                onNavigateToPlaylists = { navController.navigate(Destination.AllPlaylists.route) },
                onNavigateToFavorites = { navController.navigate(Destination.Favorites.route) },
                onNavigateToSettings = { navController.navigate(Destination.Settings.route) }
            )
        }
        
        composable(Destination.Search.route) {
            SearchScreen(
                searchViewModel = searchViewModel,
                onNavigateBack = { navController.popBackStack() },
                onTrackClick = { trackIndex ->
                    navController.navigate("${Destination.TRACK_DETAILS.route}/0")
                }
            )
        }
        
        composable(Destination.AllPlaylists.route) {
            PlaylistsScreen(
                playlistsViewModel = playlistsViewModel,
                addNewPlaylist = { navController.navigate(Destination.NewPlaylist.route) },
                navigateToPlaylist = { index -> 
                    navController.navigate(Destination.Playlist.createRoute(index)) 
                },
                navigateBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Destination.Playlist.route,
            arguments = listOf(
                navArgument("index") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: 0
            val viewModel: PlaylistViewModel = koinViewModel { parametersOf(index.toLong()) }
            
            PlaylistScreen(
                playlistViewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onTrackClick = { /* TODO: navigate to track details */ }
            )
        }
        
        composable(Destination.NewPlaylist.route) {
            // TODO: NewPlaylistScreen implementation
            navController.popBackStack()
        }
        
        composable(Destination.Favorites.route) {
            val viewModel: FavoritesViewModel = koinViewModel()
            FavoritesScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onTrackClick = { track ->
                    // TODO: Navigate to track details with track data
                }
            )
        }
        
        composable(Destination.Settings.route) {
            // TODO: SettingsScreen implementation
            navController.popBackStack()
        }
    }
}