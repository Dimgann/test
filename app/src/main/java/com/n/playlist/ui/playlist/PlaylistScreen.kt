package com.kotler.playlist.ui.playlist

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kotler.playlist.R
import com.kotler.playlist.ui.search.components.TrackListItem

@Composable
fun PlaylistScreen(
    viewModel: PlaylistViewModel,
    onNavigateBack: () -> Unit,
    onTrackClick: (Int) -> Unit
) {
    val playlist by viewModel.playlist.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Удалить плейлист?") },
            text = { Text("Этот плейлист будет удалён без возможности восстановления.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteCurrentPlaylist()
                        showDeleteDialog = false
                        onNavigateBack() // Возврат на список после удаления
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onNavigateBack) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
            
            Text(
                text = stringResource(R.string.playlist),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            
            IconButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.size(40.dp)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить плейлист",
                    tint = Color.Red
                )
            }
        }
        
        playlist?.let { pl ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(205.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.LightGray)
                        ) {
                            if (pl.coverImageUri != null) {
                                AsyncImage(
                                    model = Uri.parse(pl.coverImageUri),
                                    contentDescription = pl.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                androidx.compose.material3.Icon(
                                    painter = painterResource(R.drawable.ic_cover_photo),
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp).align(Alignment.Center),
                                    tint = Color.Gray
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(pl.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        if (pl.description.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(pl.description, fontSize = 14.sp, color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("${pl.tracks.size} треков", fontSize = 13.sp, color = Color.Gray)
                    }
                }
                
                if (pl.tracks.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_songs_found),
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    }
                } else {
                    items(pl.tracks.size) { index ->
                        TrackListItem(
                            track = pl.tracks[index],
                            onClick = { onTrackClick(index) }
                        )
                    }
                }
            }
        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}