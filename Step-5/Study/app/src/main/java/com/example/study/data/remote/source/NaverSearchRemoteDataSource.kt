package com.example.study.data.remote.source

import com.example.study.data.remote.model.Movie
import com.example.study.data.remote.model.NaverSearchResponse
import io.reactivex.Single

interface NaverSearchRemoteDataSource {
    fun getMovies(query: String): Single<List<Movie>>
}