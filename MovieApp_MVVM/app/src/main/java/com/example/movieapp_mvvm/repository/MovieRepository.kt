package com.example.movieapp_mvvm.repository

import com.example.movieapp_mvvm.api.RetrofitInstance
import com.example.movieapp_mvvm.db.MovieDataBase
import com.example.movieapp_mvvm.models.Movie

class MovieRepository(
    val db: MovieDataBase
) {
    suspend fun getPopularMovies(language: String, pageNumber: Int) =
        RetrofitInstance.api.getMovies(language, pageNumber)

    suspend fun searchMovies(language: String, searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForMovies(language, searchQuery, pageNumber)

    suspend fun upsert(movie: Movie) = db.getMovieDao().upsert(movie)

    fun getFavorite() = db.getMovieDao().getAllMovies()

    suspend fun deleteMovie(movie: Movie) = db.getMovieDao().deleteMovie(movie)
}
