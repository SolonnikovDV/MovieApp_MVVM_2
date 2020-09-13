package com.example.movielistonfragments.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.movielistonfragments.R
import com.example.movielistonfragments.db.MovieDataBase
import com.example.movielistonfragments.repository.MovieRepository
import kotlinx.android.synthetic.main.activity_movie.*


class MovieActivity : AppCompatActivity() {

    lateinit var viewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val movieRepository = MovieRepository(MovieDataBase(this))
        val viewModelProviderFactory = MovieViewModelProviderFactory(movieRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MovieViewModel::class.java)

        bottom_navigation_view.setupWithNavController(movie_nav_host_fragment.findNavController())
    }
}