package com.example.movielistonfragments.model


import com.example.movielistonfragments.model.Movie
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val page: Int,
    val movies: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)