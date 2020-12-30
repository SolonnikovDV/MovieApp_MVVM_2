package com.example.movieapp_mvvm.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
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
import com.example.movieapp_mvvm.models.Movie
import com.example.movieapp_mvvm.alarm.AlarmService
import com.example.movieapp_mvvm.ui.MovieActivity
import com.example.movieapp_mvvm.ui.MovieViewModel
import com.example.movieapp_mvvm.util.Constants.Companion.LANGUAGE_RUS
import com.example.movieapp_mvvm.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.movieapp_mvvm.util.Resource
import kotlinx.android.synthetic.main.fragment_movies.*
import java.util.*
import kotlin.collections.ArrayList

class MoviesFragment : Fragment(R.layout.fragment_movies) {

    lateinit var viewModel: MovieViewModel
    lateinit var movieAdapter: MovieAdapter



    companion object {
        const val TAG = "MoviesFragment"
        val seeLaterList: ArrayList<Movie> = ArrayList<Movie>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MovieActivity).viewModel
        setupRecyclerView()

        //init alarmService
        alarmService = AlarmService(requireContext())

        //show movie details on click in DetailsFragment
        //using nav_graph
        movieAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("details", it)
            }
            findNavController().navigate(R.id.action_moviesFragment_to_detailsFragment, bundle)
        }

        //set alarm time on click
        movieAdapter.setOnAlarmButtonClickListener {
            setAlarm{timeInMillis -> alarmService.setExactAlarm(timeInMillis)}

            //saving item in the list
            seeLaterList.add(movieAdapter.differ.currentList[it])

            Toast.makeText(
                context,
                "Movie ''${movieAdapter.differ.currentList[it].title}'' was added to reminder list",
                Toast.LENGTH_LONG
            )
                .show()
        }

        viewModel.popularMovies.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { movieResponse ->
                        movieAdapter.differ.submitList(movieResponse.movies.toList())
                        val totalPages = movieResponse.totalPages / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.popularMoviesPage == totalPages
                        if (isLastPage) {
                            rvMovieList.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG)
                            .show()
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

    lateinit var alarmService: AlarmService

    fun setAlarm(callback: (Long) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            DatePickerDialog(
                requireContext(),
                0,
                { _, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)

                    TimePickerDialog(
                        requireContext(),
                        0,
                        { _, hour, min ->
                            this.set(Calendar.HOUR_OF_DAY, hour)
                            this.set(Calendar.MINUTE, min)
                            callback(this.timeInMillis)
                        },
                        this.get(Calendar.HOUR_OF_DAY),
                        this.get(Calendar.MINUTE),
                        false
                    ).show()
                },
                this.get(Calendar.YEAR),
                this.get(Calendar.MONTH),
                this.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

}