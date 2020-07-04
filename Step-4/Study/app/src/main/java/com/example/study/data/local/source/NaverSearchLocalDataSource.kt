package com.example.study.data.local.source

import com.example.study.data.remote.model.Movie
import io.reactivex.Completable
import io.reactivex.Single

interface NaverSearchLocalDataSource {

    fun saveMovieEntity(movies: List<Movie>): Completable

    fun getMovies(): Single<List<Movie>>
}