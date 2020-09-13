package com.example.movielistonfragments.api

import com.example.movielistonfragments.model.MovieResponse
import com.example.movielistonfragments.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieAPI {

//    https://api.themoviedb.org/3/movie/popular?api_key=ecd2b50b656d93b80622dcd250837bc4&language=en-US&page=1
//    BASE_URL = "https://api.themoviedb.org/3/movie/"

    @GET("popular")
    suspend fun getMovieList(
        @Query("language")
        language: String = "ru-RUS",
        @Query("page")
        pageNumber: Int = 1,
        @Query("api_key")
        apiKey: String = API_KEY
    ): Response<MovieResponse>

//    @GET("/popular")
//    suspend fun searchAllMovies(
//        @Query("api_key")
//        apiKey: String = API_KEY,
//        @Query("q")
//        language: String,
//        @Query("page")
//        pageNumber: Int = 1
//    ) : Response<MovieResponse>
}