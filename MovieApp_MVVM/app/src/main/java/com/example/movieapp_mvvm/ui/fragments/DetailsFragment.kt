package com.example.movieapp_mvvm.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.movieapp_mvvm.R
import com.example.movieapp_mvvm.ui.MovieActivity
import com.example.movieapp_mvvm.ui.MovieViewModel
import com.example.movieapp_mvvm.util.Constants.Companion.IMAGE_PRE_PATH
import com.example.movieapp_mvvm.util.Constants.Companion.MOVIE_URL
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment(R.layout.fragment_details) {

    lateinit var viewModel: MovieViewModel
    val args: DetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MovieActivity).viewModel

        val details = args.details
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(MOVIE_URL + details.id)
        }

        fab.setOnClickListener {
            viewModel.addToFavorite(details)
            Snackbar.make(view, "Movie added to a favorite list", Snackbar.LENGTH_SHORT).show()
        }
    }
}