package com.example.movieapp_mvvm.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp_mvvm.R
import com.example.movieapp_mvvm.adapters.MovieAdapter
import com.example.movieapp_mvvm.ui.MovieActivity
import com.example.movieapp_mvvm.ui.MovieViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_search.*

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    lateinit var viewModel: MovieViewModel
    lateinit var movieAdapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MovieActivity).viewModel
        setupRecyclerView()

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val movie = movieAdapter.differ.currentList[position]
                viewModel.deleteMovie(movie)
                Snackbar.make(view, "Movie deleted from favorite list", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.addToFavorite(movie)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvFavoriteMovies)
        }

        movieAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("details", it)
            }
            findNavController().navigate(R.id.action_favoriteFragment_to_detailsFragment, bundle)
        }

        viewModel.getFavorite().observe(viewLifecycleOwner, Observer { movie ->
            movieAdapter.differ.submitList(movie)
        })
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter()
        rvFavoriteMovies.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}