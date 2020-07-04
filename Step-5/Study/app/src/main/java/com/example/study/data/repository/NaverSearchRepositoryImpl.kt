package com.example.study.data.repository

import com.example.study.data.remote.model.Movie
import com.example.study.data.remote.source.NaverSearchRemoteDataSource
import com.example.study.data.local.source.NaverSearchLocalDataSource
import io.reactivex.Single

class NaverSearchRepositoryImpl(
    private val naverSearchLocalDataSource: NaverSearchLocalDataSource,
    private val naverSearchRemoteDataSource: NaverSearchRemoteDataSource
) : NaverSearchRepository {


    override fun getMovies(query: String): Single<List<Movie>> =
        naverSearchRemoteDataSource.getMovies(query).flatMap { remoteMovies ->
            naverSearchLocalDataSource.saveMovieEntity(remoteMovies)
                .andThen(Single.just(remoteMovies))
        }

    override fun getRecentMovies(): Single<List<Movie>> =
        naverSearchLocalDataSource.getMovies()


}