package com.example.movieapp_mvvm.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp_mvvm.MovieApplication
import com.example.movieapp_mvvm.models.Movie
import com.example.movieapp_mvvm.models.MovieResponse
import com.example.movieapp_mvvm.repository.MovieRepository
import com.example.movieapp_mvvm.util.Constants.Companion.LANGUAGE_RUS
import com.example.movieapp_mvvm.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MovieViewModel(
    app: Application,
    val movieRepository: MovieRepository
) : AndroidViewModel(app) {

    val popularMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var popularMoviesPage = 1
    var popularMoviesResponse: MovieResponse? = null

    val searchMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var searchMoviesPage = 1
    var searchMoviesResponse: MovieResponse? = null

    private val language = LANGUAGE_RUS

    init {
        getPopularMovies(LANGUAGE_RUS)
    }

    fun getPopularMovies(language: String) = viewModelScope.launch {
//        popularMovies.postValue(Resource.Loading())
//        val response = movieRepository.getPopularMovies(language, popularMoviesPage)
//        popularMovies.postValue(handlePopularMovieResponse(response))
        safePopularMoviesCall(language)
    }

    fun searchMovies(searchQuery: String) = viewModelScope.launch {
//        searchMovies.postValue(Resource.Loading())
//        val response = movieRepository.searchMovies(language, searchQuery, searchMoviesPage)
//        searchMovies.postValue(handleSearchMovieResponse(response))
        safeSearchMoviesCall(searchQuery)
    }

    private fun handlePopularMovieResponse(response: Response<MovieResponse>): Resource<MovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                popularMoviesPage++
                if (popularMoviesResponse == null) {
                    popularMoviesResponse = resultResponse
                } else {
                    val oldMovies = popularMoviesResponse?.movies
                    val newMovies = resultResponse.movies
                    oldMovies?.addAll(newMovies)
                }
                return Resource.Success(popularMoviesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchMovieResponse(response: Response<MovieResponse>): Resource<MovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchMoviesPage++
                if (searchMoviesResponse == null) {
                    searchMoviesResponse = resultResponse
                } else {
                    val oldMovies = searchMoviesResponse?.movies
                    val newMovies = resultResponse.movies
                    oldMovies?.addAll(newMovies)
                }
                return Resource.Success(searchMoviesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun addToFavorite(movie: Movie) = viewModelScope.launch {
        movieRepository.upsert(movie)
    }

    fun getFavorite() = movieRepository.getFavorite()

    fun deleteMovie(movie: Movie) = viewModelScope.launch {
        movieRepository.deleteMovie(movie)
    }

    private suspend fun safePopularMoviesCall (language: String){
        popularMovies.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = movieRepository.getPopularMovies(language, popularMoviesPage)
                popularMovies.postValue(handlePopularMovieResponse(response))
            }else{
                popularMovies.postValue(Resource.Error("No internet connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException-> popularMovies.postValue((Resource.Error("Internet failure")))
                else-> popularMovies.postValue(Resource.Error("JSON conversion error"))
            }
        }
    }

    private suspend fun safeSearchMoviesCall (searchQuery: String){
        searchMovies.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = movieRepository.searchMovies(language, searchQuery, popularMoviesPage)
                searchMovies.postValue(handleSearchMovieResponse(response))
            }else{
                searchMovies.postValue(Resource.Error("No internet connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException-> searchMovies.postValue((Resource.Error("Internet failure")))
                else-> searchMovies.postValue(Resource.Error("JSON conversion error"))
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MovieApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}