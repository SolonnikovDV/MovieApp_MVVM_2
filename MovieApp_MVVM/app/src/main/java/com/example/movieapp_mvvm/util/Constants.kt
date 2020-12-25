package com.example.movieapp_mvvm.util

import com.example.movieapp_mvvm.R

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
        // for displaying a movie details in DetailsFragment
        const val MOVIE_URL = "https://www.themoviedb.org/movie/"

        //for AlarmService and AlarmReceiver
        const val EXTRA_EXACT_ALARM_TIME = "EXTRA_EXACT_ALARM_TIME"
        const val ACTION_SET_EXACT = "ACTION_SET_EXTRA"
        //extends for alarm schedule
        //set repetitive alarm (not using yet)
        const val ACTION_SET_REPETITIVE_EXACT = "ACTION_SET_REPETITIVE_EXACT"

        //for MyFirebaseMessagingService
        const val NOTIFICATION_CHANNEL_ID = "MovieApp_MVVM/app"
        const val NOTIFICATION_ID = 100
        const val NOTIFICATION_ICON = R.drawable.ic_logo

        const val CHANNEL_ID = "CHANNEL_ID"
        const val CHANNEL_NAME = "CHANNEL_NAME"
    }
}