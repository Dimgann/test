package com.kotler.playlist.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotler.playlist.R

@Composable
fun MainScreen(toSearch: () -> Unit, toPlaylists: () -> Unit, toFavorites: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(R.string.app_name), fontSize = 28.sp, modifier = Modifier.padding(bottom = 32.dp))
        Button(Modifier.fillMaxWidth(0.8f).height(56.dp), onClick = toSearch) { Text(stringResource(R.string.search), fontSize = 18.sp) }
        Spacer(Modifier.height(16.dp))
        Button(Modifier.fillMaxWidth(0.8f).height(56.dp), onClick = toPlaylists) { Text(stringResource(R.string.playlists), fontSize = 18.sp) }
        Spacer(Modifier.height(16.dp))
        Button(Modifier.fillMaxWidth(0.8f).height(56.dp), onClick = toFavorites) { Text(stringResource(R.string.favourites), fontSize = 18.sp) }
    }
}