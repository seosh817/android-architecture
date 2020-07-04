package com.example.study.ui.base

import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel {

    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun clearDisposable() {
        compositeDisposable.clear()
    }
}