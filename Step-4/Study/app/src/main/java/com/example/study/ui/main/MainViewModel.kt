package com.example.study.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.study.data.remote.model.Movie
import com.example.study.data.repository.NaverSearchRepository
import com.example.study.util.base.BaseViewModel
import com.example.study.util.extension.toRx
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.merge
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class MainViewModel(private val naverSearchRepository: NaverSearchRepository) : BaseViewModel() {

    val _query = MutableLiveData<String>()

    private val _movieItems = MutableLiveData<List<Movie>>()
    val movieItems: LiveData<List<Movie>>
        get() = _movieItems

    private val _isProgressBoolean = MutableLiveData<Boolean>()
    val isProgressBoolean: LiveData<Boolean>
        get() = _isProgressBoolean

    private val _isKeyboardBoolean = MutableLiveData<Boolean>()
    val isKeyboardBoolean: LiveData<Boolean>
        get() = _isKeyboardBoolean

    private val _errorQueryEmpty = MutableLiveData<Throwable>()
    val errorQueryEmpty: LiveData<Throwable>
        get() = _errorQueryEmpty

    private val _errorResultEmpty = MutableLiveData<Throwable>()
    val errorResultEmpty: LiveData<Throwable>
        get() = _errorResultEmpty

    private val _errorFailSearch = MutableLiveData<Throwable>()
    val errorFailSearch: LiveData<Throwable>
        get() = _errorFailSearch

    val buttonClickSubject = PublishSubject.create<Unit>()


    init {
        val click = buttonClickSubject
            .map { _query.value }
            .throttleFirst(1000L, TimeUnit.MILLISECONDS)


        val textChange = _query.toRx.debounce(
            1500L,
            TimeUnit.MILLISECONDS,
            AndroidSchedulers.mainThread()
        )

        listOf(textChange, click)
            .merge()
            .filter(String::isNotBlank)
            .distinctUntilChanged()
            .subscribe(::getMovies)
            .addTo(compositeDisposable)
    }

    private fun getMovies(query: String) {
        query.let { query ->
            if (query.isBlank()) {
                _errorQueryEmpty.value = Throwable()
            } else {
                (naverSearchRepository.getMovies(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        showProgress()
                    }
                    .doAfterTerminate {
                        hideProgress()
                        hideKeyBoard()
                    }
                    .doOnError {
                        hideProgress()
                    }
                    .subscribe({
                        it?.let {
                            if (it.isNotEmpty()) {
                                _movieItems.postValue(it)
                            } else {
                                _errorResultEmpty.value = Throwable()
                            }
                        }
                    }, {
                        it.printStackTrace()
                        _errorFailSearch.value = it
                    })
                        ).addTo(compositeDisposable)
            }
        }
    }


    fun getRecentSearchResult() {
        naverSearchRepository.getRecentMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                showProgress()
            }.doAfterTerminate {
                hideProgress()
                hideKeyBoard()
            }
            .subscribe({
                _movieItems.value = it
            }, {
                _errorFailSearch.value = it
            }).addTo(compositeDisposable)
    }

    private fun showProgress() {
        _isProgressBoolean.value = true
    }

    private fun hideProgress() {
        _isProgressBoolean.value = false
    }

    private fun hideKeyBoard() {
        _isKeyboardBoolean.value = false
    }
}