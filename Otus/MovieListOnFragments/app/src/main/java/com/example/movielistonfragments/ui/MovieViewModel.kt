package com.example.movielistonfragments.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielistonfragments.model.MovieResponse
import com.example.movielistonfragments.repository.MovieRepository
import com.example.movielistonfragments.util.Constants.Companion.API_KEY
import com.example.movielistonfragments.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MovieViewModel(
    val movieRepository: MovieRepository
) : ViewModel() {

    val movieList: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val movieListPage = 1

    init{
        getMovieList("ru-RUS")
    }

    fun getMovieList(language: String) = viewModelScope.launch {
        movieList.postValue(Resource.Loading())
        val response= movieRepository.getMovieList(language, movieListPage)
        movieList.postValue(handleMovieListResponse(response))
    }

    // check response status
    private fun handleMovieListResponse(response: Response<MovieResponse>): Resource<MovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}