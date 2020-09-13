package com.example.movieapp_mvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.movieapp_mvvm.R
import com.example.movieapp_mvvm.db.MovieDataBase
import com.example.movieapp_mvvm.repository.MovieRepository
import kotlinx.android.synthetic.main.activity_movie.*

class MovieActivity : AppCompatActivity() {

    lateinit var viewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val moviesRepository = MovieRepository(MovieDataBase(this))
        val viewModelProviderFactory = MovieViewModelProviderFactory(moviesRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MovieViewModel::class.java)
        bottomNavigationView.setupWithNavController(moviesNavHostFragment.findNavController())
    }
}