package com.example.study.data.local.source

import com.example.study.data.remote.model.Movie
import com.example.study.data.local.dao.MovieEntityDao
import com.example.study.data.local.model.MovieEntity
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class NaverSearchLocalDataSourceImpl private constructor(
    private val naverResultDao: MovieEntityDao
) : NaverSearchLocalDataSource {

    override fun saveMovieEntity(movies: List<Movie>): Completable =
        naverResultDao.deleteAll()
            .andThen(Single.just(movies))
            .map {
                it.map {
                    MovieEntity(
                        id = 0L,
                        actor = it.actor,
                        director = it.director,
                        image = it.image,
                        link = it.link,
                        pubDate = it.pubDate,
                        subtitle = it.subtitle,
                        title = it.title,
                        userRating = it.userRating
                    )
                }
            }
            .flatMapCompletable(naverResultDao::saveMovieEntity)
            .subscribeOn(Schedulers.io())

    override fun getMovies(): Single<List<Movie>> = naverResultDao.getMovieEntities()
        .map {
            it.map {
                Movie(
                    actor = it.actor,
                    director = it.director,
                    image = it.image,
                    link = it.link,
                    pubDate = it.pubDate,
                    subtitle = it.subtitle,
                    title = it.title,
                    userRating = it.userRating
                )
            }
        }.subscribeOn(Schedulers.io())


    companion object {
        private var instance: NaverSearchLocalDataSourceImpl? = null

        fun getInstance(naverResultDao: MovieEntityDao): NaverSearchLocalDataSourceImpl {
            return instance
                ?: synchronized(NaverSearchLocalDataSourceImpl::javaClass) {
                instance
                    ?: NaverSearchLocalDataSourceImpl(
                        naverResultDao
                    )
                        .also { instance = it }
            }

        }
    }
}