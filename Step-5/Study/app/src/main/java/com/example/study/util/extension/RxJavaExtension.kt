package com.example.study.util.extension

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    this.add(disposable)
}

val <T> MutableLiveData<T>.toRx: Observable<T>
    get() = Observable.create { emitter ->

        val observer: Observer<T> = Observer {
            if(!emitter.isDisposed && value != null) {
                emitter.onNext(value!!)
            }
        }

        this.observeForever(observer)

        emitter.setCancellable {
            this.removeObserver(observer)
        }
    }