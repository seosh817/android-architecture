package com.example.study.data.remote.model

import com.google.gson.annotations.SerializedName

data class NaverSearchResponse(
    @SerializedName("total")
    val total: String,
    @SerializedName("start")
    val start: String,
    @SerializedName("display")
    val display: String,
    @SerializedName("items")
    val items: List<Movie>
)