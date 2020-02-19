package com.example.study.data.repository

import com.example.study.data.model.Movie
import com.example.study.data.model.NaverSearchResponse
import com.example.study.data.source.local.SearchResultDatabase
import com.example.study.data.source.local.model.SearchResult
import io.reactivex.Single

interface NaverSearchRepository {
    //remote
    fun getMovies(query: String): Single<NaverSearchResponse>

    //local
    fun addSearchResult(searchResult: SearchResult)
    fun getRecentSearchResult() : List<Movie>?
}
