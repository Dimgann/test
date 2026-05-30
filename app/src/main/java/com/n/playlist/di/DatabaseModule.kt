package com.kotler.playlist.di

import com.kotler.playlist.data.database.AppDatabase
import com.kotler.playlist.data.preferences.SearchHistoryPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        androidx.room.Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "playlist_maker.db"
        ).build()
    }
    
    single { get<AppDatabase>().tracksDao() }
    single { get<AppDatabase>().playlistsDao() }
    
    single { SearchHistoryPreferences(androidContext()) }
}