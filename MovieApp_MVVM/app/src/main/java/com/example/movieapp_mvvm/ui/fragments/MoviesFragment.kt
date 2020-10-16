package com.example.movieapp_mvvm.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp_mvvm.R
import com.example.movieapp_mvvm.adapters.MovieAdapter
import com.example.movieapp_mvvm.models.MovieResponse
import com.example.movieapp_mvvm.ui.MovieActivity
import com.example.movieapp_mvvm.ui.MovieViewModel
import com.example.movieapp_mvvm.util.Constants.Companion.LANGUAGE_RUS
import com.example.movieapp_mvvm.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.movieapp_mvvm.util.Resource
import kotlinx.android.synthetic.main.fragment_movies.*

class MoviesFragment : Fragment(R.layout.fragment_movies) {

    lateinit var viewModel: MovieViewModel
    lateinit var movieAdapter: MovieAdapter

    companion object {
        const val TAG = "MoviesFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MovieActivity).viewModel
        setupRecyclerView()

        movieAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("details", it)
            }
            findNavController().navigate(R.id.action_moviesFragment_to_detailsFragment, bundle)
        }

        viewModel.popularMovies.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { movieResponse ->
                        movieAdapter.differ.submitList(movieResponse.movies.toList())
                        val totalPages = movieResponse.totalPages / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.popularMoviesPage == totalPages
                        if(isLastPage){
                            rvMovieList.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    //region Pagination
    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPosition = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThenVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPosition && isAtLastItem && isNotAtBeginning && isTotalMoreThenVisible
            if (shouldPaginate) {
                viewModel.getPopularMovies(LANGUAGE_RUS)
                isScrolling = false
            }
        }
    }
    //endregion

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter()
        rvMovieList.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@MoviesFragment.scrollListener)
        }
    }

}