package com.example.study.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.Observable
import com.example.study.R
import com.example.study.data.local.MovieDatabase
import com.example.study.data.local.source.NaverSearchLocalDataSourceImpl
import com.example.study.data.remote.source.NaverSearchRemoteDataSourceImpl
import com.example.study.data.repository.NaverSearchRepositoryImpl
import com.example.study.databinding.ActivityMainBinding
import com.example.study.ui.adapter.MovieAdapter
import com.example.study.ui.base.BaseActivity
import com.example.study.util.extension.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    override val vm: MainViewModel by lazy {
        MainViewModel(
            NaverSearchRepositoryImpl.getInstance(
                NaverSearchLocalDataSourceImpl.getInstance(MovieDatabase.getInstance(this)!!.MovieEntityDao())
                , NaverSearchRemoteDataSourceImpl.getInstance()
            )
        )
    }

    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter()
    }

    private val compositeDisposable = CompositeDisposable()
    private val buttonClickSubject = PublishSubject.create<Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this
        binding.rvMovieList.adapter = movieAdapter
        getRecentSearchResult()
        addObserveProperty()
    }

    private fun getRecentSearchResult() {
        vm.getRecentMovies()
    }

    private fun showErrorQueryEmpty() {
        Toast.makeText(applicationContext, R.string.empty_query_message, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorEmptyResult() {
        Toast.makeText(applicationContext, R.string.empty_result_message, Toast.LENGTH_SHORT).show()
    }

    private fun failSearch() {
        Toast.makeText(applicationContext, R.string.fail_search, Toast.LENGTH_SHORT).show()
    }

    private fun addObserveProperty() {

        vm.errorQueryEmpty.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                showErrorQueryEmpty()
            }
        })

        vm.errorFailSearch.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                failSearch()
            }
        })

        vm.errorResultEmpty.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                showErrorEmptyResult()
            }
        })

        vm.isKeyboardBoolean.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                vm.isKeyboardBoolean.get()?.let {
                    if (!it) {
                        hideKeyboard()
                    }
                }
            }
        })

        vm.isProgressBoolean.addOnPropertyChangedCallback(object :
        Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                vm.isProgressBoolean.get()?.let {
                    it.let {
                        if(it) {
                            binding.pbLoading.visibility = View.VISIBLE
                        } else {
                            binding.pbLoading.visibility = View.GONE
                        }
                    }
                }
            }
        })
    }

    private fun bindViewModel() {
        compositeDisposable += buttonClickSubject
            .observeOn(AndroidSchedulers.mainThread())
            .throttleFirst(2000L, TimeUnit.MILLISECONDS)
            .subscribe({
                vm.getMovies()
            }, {
                Toast.makeText(applicationContext, R.string.show_rx_error, Toast.LENGTH_SHORT).show()
            })
    }


    fun getMovies() {
        buttonClickSubject.onNext(Unit)
    }

    override fun onStart() {
        super.onStart()
        bindViewModel()
    }

    override fun onPause() {
        compositeDisposable.clear()
        super.onPause()
    }

}




