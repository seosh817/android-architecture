package com.example.study.ui.main

import com.example.study.data.remote.model.Movie
import com.example.study.util.base.BaseContract

interface MainContract {

    interface View : BaseContract.View {
        fun updateMovieList(items: List<Movie>)
        fun showErrorQueryEmpty()
        fun showErrorEmptyResult()
        fun showErrorRxError()
    }

    interface Presenter : BaseContract.Presenter {
        fun getMovies(query: String)
        fun getRecentMovies()
    }

}