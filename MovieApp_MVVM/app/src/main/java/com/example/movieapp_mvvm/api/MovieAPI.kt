package com.example.movieapp_mvvm.api

import com.example.movieapp_mvvm.models.MovieResponse
import com.example.movieapp_mvvm.util.Constants.Companion.API_KEY
import com.example.movieapp_mvvm.util.Constants.Companion.POPULAR_REQUEST
import com.example.movieapp_mvvm.util.Constants.Companion.SEARCH_REQUEST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.themoviedb.org/3/movie/popular?api_key=ecd2b50b656d93b80622dcd250837bc4&language=ru-RUS&page=1
//https://api.themoviedb.org/3/search/movie?api_key=ecd2b50b656d93b80622dcd250837bc4&language=ru-RUS&page=1&include_adult=false


interface MovieAPI {

    @GET(POPULAR_REQUEST)
    suspend fun getMovies(

        @Query("language")
        language: String = "ru-RUS",
        @Query("page")
        page: Int = 1,
        @Query("api_key")
        apiKey: String = API_KEY
    ): Response<MovieResponse>

    @GET(SEARCH_REQUEST)
    suspend fun searchForMovies(
        @Query("language")
        language: String = "ru-RUS",
        @Query("query")
        query: String,
        @Query("page")
        page: Int = 1,
        @Query("api_key")
        apiKey: String = API_KEY
    ): Response<MovieResponse>
}