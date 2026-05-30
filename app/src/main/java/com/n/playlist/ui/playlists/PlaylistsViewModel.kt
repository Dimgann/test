package com.kotler.playlist.ui.playlists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kotler.playlist.R
import com.kotler.playlist.ui.search.components.TrackListItem
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotler.playlist.domain.model.Playlist
import com.kotler.playlist.domain.repository.PlaylistsRepository
import com.kotler.playlist.domain.repository.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun PlaylistsScreen(viewModel: PlaylistsViewModel, onCreate: () -> Unit, onNavigate: (Long) -> Unit, onBack: () -> Unit) {
    val lists by viewModel.playlists.collectAsState(emptyList())
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back)) }
            Text(stringResource(R.string.playlists), fontSize = 24.sp, modifier = Modifier.padding(start = 8.dp))
        }
        Box(Modifier.fillMaxSize()) {
            if (lists.isEmpty()) SearchPlaceholder(stringResource(R.string.no_playlists))
            else LazyColumn { items(lists.size) { idx -> val p = lists[idx]; TrackListItem(com.kotler.playlist.domain.model.Track(p.id, p.name, p.description, "${p.tracks.size} треков", "", false, 0)) { onNavigate(p.id) } } }
            FloatingActionButton(Modifier.padding(24.dp).align(Alignment.BottomEnd), onClick = onCreate, containerColor = MaterialTheme.colorScheme.primary) { Icon(Icons.Default.Add, null) }
        }
    }
}

class PlaylistsViewModel(
    private val playlistsRepository: PlaylistsRepository,
    private val tracksRepository: TracksRepository
) : ViewModel() {
    
    val playlists: Flow<List<Playlist>> = playlistsRepository.getAllPlaylists()
    
    fun createPlaylist(name: String, description: String, coverUri: String? = null) {
        viewModelScope.launch {
            playlistsRepository.addNewPlaylist(name, description, coverUri)
        }
    }
    
    fun deletePlaylist(id: Long) {
        viewModelScope.launch {
            playlistsRepository.deletePlaylistById(id)
        }
    }
}