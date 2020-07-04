package com.example.study.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.study.data.local.dao.MovieEntityDao
import com.example.study.data.local.model.MovieEntity

@Database(version = 1, entities = [MovieEntity::class], exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun MovieEntityDao(): MovieEntityDao


    companion object {

        private var instance: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase? {

            return instance
                ?: synchronized(MovieDatabase::class) {
                instance ?: Room.databaseBuilder(context.applicationContext, MovieDatabase::class.java, "movie.db").allowMainThreadQueries().build().apply { instance = this}
            }


        }
    }
}