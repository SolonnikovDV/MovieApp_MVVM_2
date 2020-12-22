package com.example.movieapp_mvvm.util

class Constants {
    companion object{
        const val API_KEY = "ecd2b50b656d93b80622dcd250837bc4"
        const val BASE_URL = "https://api.themoviedb.org"
        const val POPULAR_REQUEST = "/3/movie/popular"
        const val SEARCH_REQUEST = "/3/search/movie"
        const val SEARCH_MOVIES_QUERY_DELAY = 500L
        const val QUERY_PAGE_SIZE = 20
        const val LANGUAGE_RUS = "ru-RUS"

        // pre-path for Glide to get image from tmdb
        const val IMAGE_PRE_PATH = "https://image.tmdb.org/t/p/w500"

        const val MOVIE_URL = "https://www.themoviedb.org/movie/"

        const val ACTION_SET_EXACT_ALARM = "ACTION_SET_EXACT_ALARM"
        const val EXTRA_EXACT_ALARM_TIME = "EXTRA_EXACT_ALARM_TIME"

    }
}