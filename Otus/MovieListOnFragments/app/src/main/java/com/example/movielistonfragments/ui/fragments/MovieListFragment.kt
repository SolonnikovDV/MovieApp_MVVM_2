package com.example.movielistonfragments.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movielistonfragments.R
import com.example.movielistonfragments.adapters.MovieListAdapter
import com.example.movielistonfragments.ui.MovieActivity
import com.example.movielistonfragments.ui.MovieViewModel
import com.example.movielistonfragments.util.Resource
import kotlinx.android.synthetic.main.fragment_movie_list.*

class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    lateinit var viewModel: MovieViewModel
    lateinit var movieListAdapter: MovieListAdapter

    private val TAG = "MovieListFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MovieActivity).viewModel

        setupRecyclerView()

        viewModel.movieList.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { movieResponse ->
                        movieListAdapter.differ.submitList(movieResponse.movies)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        pagination_progress_bar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        pagination_progress_bar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        movieListAdapter = MovieListAdapter()
        recycler_view_movie_list.apply {
            adapter = movieListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}