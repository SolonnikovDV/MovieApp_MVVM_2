package com.example.movielistonfragments.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.movielistonfragments.R
import com.example.movielistonfragments.ui.MovieActivity
import com.example.movielistonfragments.ui.MovieViewModel

class FavoriteFragment: Fragment(R.layout.fragment_favorite) {

    lateinit var viewModel: MovieViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MovieActivity).viewModel
    }
}