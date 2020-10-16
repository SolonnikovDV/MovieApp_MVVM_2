package com.example.movieapp_mvvm.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp_mvvm.R
import com.example.movieapp_mvvm.adapters.MovieAdapter
import com.example.movieapp_mvvm.ui.MovieActivity
import com.example.movieapp_mvvm.ui.MovieViewModel
import com.example.movieapp_mvvm.util.Constants
import com.example.movieapp_mvvm.util.Constants.Companion.SEARCH_MOVIES_QUERY_DELAY
import com.example.movieapp_mvvm.util.Resource
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.paginationProgressBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search) {

    lateinit var viewModel: MovieViewModel
    lateinit var movieAdapter: MovieAdapter

    companion object {
        const val TAG = "SearchFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MovieActivity).viewModel
        setupRecyclerView()

        movieAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("details", it)
            }
            findNavController().navigate(R.id.action_searchFragment_to_detailsFragment, bundle)
        }

        var job: Job? = null

        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_MOVIES_QUERY_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.searchMovies(editable.toString())
                    }
                }
            }
        }


        viewModel.searchMovies.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { movieResponse ->
                        movieAdapter.differ.submitList(movieResponse.movies.toList())
                        val totalPages = movieResponse.totalPages / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchMoviesPage == totalPages
                        if(isLastPage){
                            rvSearchMovies.setPadding(0,0,0,0)
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
            val isTotalMoreThenVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPosition && isAtLastItem && isNotAtBeginning && isTotalMoreThenVisible
            if (shouldPaginate) {
                viewModel.searchMovies(etSearch.text.toString())
                isScrolling = false
            }
        }
    }
    //endregion

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter()
        rvSearchMovies.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchFragment.scrollListener)
        }
    }
}