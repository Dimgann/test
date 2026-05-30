package com.kotler.playlist.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotler.playlist.R
import com.kotler.playlist.ui.search.components.SearchPlaceholder
import com.kotler.playlist.ui.search.components.TrackListItem

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel,
    onNavigateBack: () -> Unit,
    onTrackClick: (Int) -> Unit
) {
    val state by searchViewModel.searchState.collectAsState()
    var searchText by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    
    LaunchedEffect(searchText) {
        searchViewModel.updateQuery(searchText)
    }
    
    LaunchedEffect(state) {
        if (state is SearchState.Success || state is SearchState.Empty) {
            focusManager.clearFocus()
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
            Text(
                text = stringResource(R.string.search),
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        // Search field
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { isFocused = it.isFocused },
            placeholder = {
                Text(stringResource(R.string.search_songs_placeholder))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_icon)
                )
            },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = {
                        searchText = ""
                        searchViewModel.clearSearch()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear_search)
                        )
                    }
                }
            },
            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true
        )
        
        // Content
        when (val currentState = state) {
            is SearchState.Initial, is SearchState.Loading -> {
                if (currentState is SearchState.Loading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (searchText.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.search_placeholder),
                            color = Color.Gray
                        )
                    }
                }
            }
            
            is SearchState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(currentState.tracks) { track ->
                        TrackListItem(
                            track = track,
                            onClick = { onTrackClick(currentState.tracks.indexOf(track)) }
                        )
                    }
                }
            }
            
            is SearchState.Empty -> {
                SearchPlaceholder(
                    message = stringResource(R.string.no_songs_found),
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            is SearchState.Error -> {
                SearchPlaceholder(
                    message = currentState.message,
                    showRetry = true,
                    onRetry = { searchViewModel.retrySearch() },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}