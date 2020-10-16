package com.example.movieapp_mvvm.models


import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val page: Int,
    @SerializedName("results")
    val movies: MutableList<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)