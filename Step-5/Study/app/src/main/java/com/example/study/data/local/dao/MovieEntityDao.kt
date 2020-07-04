package com.example.study.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.study.data.local.model.MovieEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MovieEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovieEntity(movies: List<MovieEntity>): Completable

    @Query("SELECT * FROM movie_entity")
    fun getMovieEntities(): Single<List<MovieEntity>>

    @Query("DELETE FROM movie_entity")
    fun deleteAll() : Completable
}