package com.example.movieapp_mvvm.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.movieapp_mvvm.R
import com.example.movieapp_mvvm.ui.MovieActivity
import com.example.movieapp_mvvm.ui.MovieViewModel

class DetailsFragment : Fragment(R.layout.fragment_details) {

    lateinit var viewModel: MovieViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MovieActivity).viewModel
    }
}