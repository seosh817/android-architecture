package com.example.study.data.remote.source

import com.example.study.data.remote.model.Movie
import com.example.study.data.remote.network.NaverApiClient
import com.example.study.data.remote.network.NaverApiService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class NaverSearchRemoteDataSourceImpl(
    private val naverApiservice: NaverApiService
) :
    NaverSearchRemoteDataSource {

    override fun getMovies(
        query: String
    ): Single<List<Movie>> =
        naverApiservice.getMovieList(query)
            .map { it.items }
            .subscribeOn(Schedulers.io())


}