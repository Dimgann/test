package com.kotler.playlist.di

import com.kotler.playlist.data.database.AppDatabase
import com.kotler.playlist.data.preferences.SearchHistoryPreferences
import com.kotler.playlist.data.repository.PlaylistsRepositoryImpl
import com.kotler.playlist.data.repository.SearchHistoryRepositoryImpl
import com.kotler.playlist.data.repository.TracksRepositoryImpl
import com.kotler.playlist.domain.repository.PlaylistsRepository
import com.kotler.playlist.domain.repository.SearchHistoryRepository
import com.kotler.playlist.domain.repository.TracksRepository
import com.kotler.playlist.ui.favorites.FavoritesViewModel
import com.kotler.playlist.ui.playlist.PlaylistViewModel
import com.kotler.playlist.ui.playlists.PlaylistsViewModel
import com.kotler.playlist.ui.search.SearchViewModel
import com.kotler.playlist.ui.shared.SharedTrackViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        androidx.room.Room.databaseBuilder(androidContext(), AppDatabase::class.java, "playlist_maker.db").build()
    }
    single { SearchHistoryPreferences(androidContext()) }
}

val repositoryModule = module {
    single<TracksRepository> { TracksRepositoryImpl(get()) }
    single<PlaylistsRepository> { PlaylistsRepositoryImpl(get()) }
    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModel { SearchViewModel(get(), get()) }
    viewModel { PlaylistsViewModel(get(), get()) }
    viewModel { (id: Long) -> PlaylistViewModel(get(), get(), id) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { SharedTrackViewModel() }
    viewModel { NewPlaylistViewModel(get()) }
}

val appModule = databaseModule + repositoryModule + viewModelModule