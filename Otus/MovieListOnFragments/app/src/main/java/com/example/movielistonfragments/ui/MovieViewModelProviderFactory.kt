package com.example.movielistonfragments.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movielistonfragments.repository.MovieRepository

class MovieViewModelProviderFactory(
    val movieRepository: MovieRepository
) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieViewModel(movieRepository) as T
    }
}