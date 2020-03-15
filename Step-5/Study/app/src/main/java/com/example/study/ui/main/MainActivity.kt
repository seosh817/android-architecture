package com.example.study.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.study.R
import com.example.study.data.model.Movie
import com.example.study.data.repository.NaverSearchRepositoryImpl
import com.example.study.data.source.local.NaverSearchLocalDataSourceImpl
import com.example.study.data.source.local.SearchResultDatabase
import com.example.study.data.source.remote.NaverSearchRemoteDataSourceImpl
import com.example.study.databinding.ActivityMainBinding
import com.example.study.ui.adapter.MovieAdapter
import com.example.study.util.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter()
    }

    override val vm: MainViewModel by viewModel()

/*    private val viewModel by viewModel<MainViewModel> {
        parametersOf(arguments?.getString(ARG_TITLE) ?: "")
    }*/ // 매개변수 넘겨줄 때는 이렇게


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.rvMovieList.adapter = movieAdapter
        getRecentSearchResult()
        addLiveDataObserve()
    }

    private fun getRecentSearchResult() {
        vm.getRecentSearchResult()
    }

    private fun showErrorQueryEmpty() {
        Toast.makeText(applicationContext, R.string.empty_query_message, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorEmptyResult() {
        Toast.makeText(applicationContext, R.string.empty_result_message, Toast.LENGTH_SHORT).show()
    }

    private fun addLiveDataObserve() {
        vm.movieItems.observe(this, Observer { movieAdapter.setItem(it) })

        vm.errorQueryEmpty.observe(this, Observer { showErrorQueryEmpty() })

        vm.errorFailSearch.observe(
            this,
            Observer {
                Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_SHORT).show()
            })

        vm.errorResultEmpty.observe(this, Observer { showErrorEmptyResult() })

        vm.isKeyboardBoolean.observe(this, Observer { if (!it) hideKeyboard() })

    }
}




