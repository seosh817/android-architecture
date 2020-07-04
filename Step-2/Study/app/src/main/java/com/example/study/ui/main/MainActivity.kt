package com.example.study.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.study.R
import com.example.study.data.remote.model.Movie
import com.example.study.data.repository.NaverSearchRepositoryImpl
import com.example.study.data.local.MovieDatabase
import com.example.study.data.local.source.NaverSearchLocalDataSourceImpl
import com.example.study.data.remote.source.NaverSearchRemoteDataSourceImpl
import com.example.study.ui.adapter.MovieAdapter
import com.example.study.ui.detail.DetailActivity
import com.example.study.util.extension.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), MainContract.View {

    private val movieAdapter = MovieAdapter()
    private val presenter: MainContract.Presenter by lazy {
        MainPresenter(
            this, NaverSearchRepositoryImpl.getInstance(
                NaverSearchLocalDataSourceImpl.getInstance(
                    MovieDatabase.getInstance(this)!!.MovieEntityDao()
                )
                , NaverSearchRemoteDataSourceImpl.getInstance()
            )
        )
    }
    private val compositeDisposable = CompositeDisposable()
    private val buttonClickSubject = PublishSubject.create<Unit>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    fun initView() {
        getRecentSearchResult()
        rv_movie_list.adapter = movieAdapter

        btn_search.setOnClickListener {
            buttonClickSubject.onNext(Unit)
        }


        movieAdapter.setOnItemClickListener { movie ->
            Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.MOVIE_URL, movie.link)
                startActivity(this)
            }
        }
    }

    private fun bindViewModel() {
        compositeDisposable += buttonClickSubject
            .observeOn(AndroidSchedulers.mainThread())
            .throttleFirst(2000L, TimeUnit.MILLISECONDS)
            .subscribe({
                if (et_movie_search.text.isNullOrEmpty()) {
                        showErrorQueryEmpty()
                } else {
                    getMovieList(et_movie_search.text.toString())
                }
            }, {
                showErrorRxError()
            })
    }


    private fun getRecentSearchResult() {
        presenter.getRecentMovies()
    }

    private fun getMovieList(query: String) {
        presenter.getMovies(query)
    }

    override fun updateMovieList(items: List<Movie>) {
        movieAdapter.setItem(items)
    }

    override fun showProgress() {
        pb_loading.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        pb_loading.visibility = View.GONE
    }

    override fun showErrorQueryEmpty() {
        Toast.makeText(this@MainActivity, R.string.empty_query_message, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorEmptyResult() {
        Toast.makeText(this@MainActivity, R.string.empty_result_message, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorRxError() {
        Toast.makeText(this@MainActivity, R.string.rx_error_message, Toast.LENGTH_SHORT).show()
    }

    override fun hideKeyboard() {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).run {
            hideSoftInputFromWindow(et_movie_search.windowToken, 0)
        }
    }

    override fun onStart() {
        super.onStart()
        bindViewModel()
    }

    override fun onPause() {
        compositeDisposable.clear()
        super.onPause()
    }

    override fun onDestroy() {
        presenter.clearDisposable()
        super.onDestroy()
    }
}




