package com.example.movieapp_mvvm.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp_mvvm.models.MovieResponse
import com.example.movieapp_mvvm.repository.MovieRepository
import com.example.movieapp_mvvm.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MovieViewModel(
    val movieRepository: MovieRepository
): ViewModel() {

    val movies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var moviesPage = 1

    init {
        getMovies("ru-RUS")
    }

    fun getMovies(language: String) = viewModelScope.launch {
        movies.postValue(Resource.Loading())
        val response = movieRepository.getMovies(language, moviesPage)
        movies.postValue(handleMoviesResponse(response))
    }

    private fun handleMoviesResponse (response: Response<MovieResponse>): Resource<MovieResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}