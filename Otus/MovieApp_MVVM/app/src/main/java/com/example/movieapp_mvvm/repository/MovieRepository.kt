package com.example.movieapp_mvvm.repository

import com.example.movieapp_mvvm.api.RetrofitInstance
import com.example.movieapp_mvvm.db.MovieDataBase

class MovieRepository (
    val db: MovieDataBase
){
    suspend fun getMovies(language: String, pageNumber: Int) =
        RetrofitInstance.api.getMovies(language, pageNumber)
}
