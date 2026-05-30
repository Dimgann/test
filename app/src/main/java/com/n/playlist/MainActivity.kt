package com.kotler.playlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kotler.playlist.presentation.MainScreen
import com.kotler.playlist.ui.theme.PlaylistTheme

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlaylistMakerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val searchViewModel: SearchViewModel by viewModel {
                        SearchViewModel(
                            tracksRepository = com.kotler.playlist.data.repository.TracksRepositoryImpl(
                                scope = androidx.lifecycle.viewModelScope
                            )
                        )
                    }
                    val playlistsViewModel: PlaylistsViewModel by viewModel()
                    
                    AppNavHost(
                        navController = androidx.navigation.compose.rememberNavController(),
                        modifier = Modifier.padding(innerPadding),
                        searchViewModel = searchViewModel,
                        playlistsViewModel = playlistsViewModel
                    )
                }
            }
        }
    }
}