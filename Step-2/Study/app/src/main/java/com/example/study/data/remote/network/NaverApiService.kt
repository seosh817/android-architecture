package com.example.study.data.remote.network

import com.example.study.data.remote.model.NaverSearchResponse
import io.reactivex.Single
import retrofit2.http.*

interface NaverApiService {

/*    @Headers(
        "X-Naver-Client-Id: AZeVMtYlsaS7bdr8W7PX",
        "X-Naver-Client-Secret: a7hDdCsKST"
    )*/

    @GET("v1/search/movie.json")
    fun getMovieList(
        @Query("query") query: String
    ): Single<NaverSearchResponse>
}