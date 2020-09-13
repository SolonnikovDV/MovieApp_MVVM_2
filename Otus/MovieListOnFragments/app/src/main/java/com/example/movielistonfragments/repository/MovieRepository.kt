package com.example.movielistonfragments.repository

import com.example.movielistonfragments.api.RetrofitInstance
import com.example.movielistonfragments.db.MovieDataBase

class MovieRepository (val db: MovieDataBase) {

    suspend fun getMovieList (language: String, pageNumber: Int) =
        RetrofitInstance.api.getMovieList(language, pageNumber)
}