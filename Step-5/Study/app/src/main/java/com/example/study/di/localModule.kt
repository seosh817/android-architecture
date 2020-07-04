package com.example.study.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.study.data.local.MovieDatabase
import com.example.study.data.local.source.NaverSearchLocalDataSource
import com.example.study.data.local.source.NaverSearchLocalDataSourceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val localModule = module {


    single {
        Room.databaseBuilder(androidContext(), MovieDatabase::class.java, "SearchResult.db")
            .allowMainThreadQueries().build()
    }

    single {
        get<MovieDatabase>().MovieEntityDao()
    }

    single<NaverSearchLocalDataSource> {
        NaverSearchLocalDataSourceImpl(get())
    }
}