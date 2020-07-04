package com.example.study.data.repository

import com.example.study.data.remote.model.Movie
import com.example.study.data.remote.model.NaverSearchResponse
import com.example.study.data.local.model.MovieEntity
import io.reactivex.Completable
import io.reactivex.Single

interface NaverSearchRepository {

    fun getMovies(query: String): Single<List<Movie>>

    fun getRecentMovies() : Single<List<Movie>>
}
