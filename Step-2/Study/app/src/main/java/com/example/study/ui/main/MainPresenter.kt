package com.example.study.ui.main

import com.example.study.data.repository.NaverSearchRepository
import com.example.study.util.extension.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainPresenter(
    private val view: MainContract.View,
    private val naverSearchRepository: NaverSearchRepository
) : MainContract.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()


    override fun getMovies(query: String) {
        compositeDisposable += (naverSearchRepository.getMovies(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.showProgress()
            }
            .doAfterTerminate {
                view.hideProgress()
            }
            .subscribe({
                it?.let {
                    if (it.isNotEmpty()) {
                        view.updateMovieList(it)
                        view.hideKeyboard()
                    } else {
                        view.showErrorEmptyResult()
                    }
                }
            }, {
                it.printStackTrace()
            })
                )
    }

    override fun getRecentMovies() {
        compositeDisposable += naverSearchRepository.getRecentMovies().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.showProgress()
            }
            .doAfterTerminate {
                view.hideProgress()
            }.subscribe({
                it?.let {
                    if (it.isNotEmpty()) {
                        view.updateMovieList(it)
                        view.hideKeyboard()
                    } else {
                        view.showErrorEmptyResult()
                    }
                }
            }, {
                it.printStackTrace()
            })
    }


    override fun clearDisposable() {
        compositeDisposable.clear()
    }
}