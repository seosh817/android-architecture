package com.example.study.data.remote.source

import com.example.study.data.remote.model.Movie
import com.example.study.data.remote.network.NaverApiClient
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class NaverSearchRemoteDataSourceImpl :
    NaverSearchRemoteDataSource {

    override fun getMovies(
        query: String
    ): Single<List<Movie>> {
        return NaverApiClient.naverRetrofitService.getMovieList(query)
            .map { it.items }
            .subscribeOn(Schedulers.io())
    }

    companion object {
        private var instance: NaverSearchRemoteDataSourceImpl? = null

        fun getInstance(): NaverSearchRemoteDataSourceImpl {
            return instance
                ?: synchronized(NaverSearchRemoteDataSourceImpl::javaClass) {
                instance
                    ?: NaverSearchRemoteDataSourceImpl()
                        .also { instance = it }
            }

        }
    }
}