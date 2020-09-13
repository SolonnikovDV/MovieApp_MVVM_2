package com.example.movielistonfragments.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movielistonfragments.R
import com.example.movielistonfragments.model.Movie
import kotlinx.android.synthetic.main.item_movie_list.view.*

class MovieListAdapter : RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>() {

    inner class MovieListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        return MovieListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_movie_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        val movie = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(movie.posterPath).into(movie_image)
            movie_name.text = movie.title
            movie_release_date.text = movie.releaseDate
            movie_popularity.text = movie.popularity.toString()
            movie_overview.text = movie.overview
            movie_vote_average.text = movie.voteAverage.toString()
            movie_vote_count.text = movie.voteCount.toString()

            setOnItemClickListener {
                onItemClickListener?.let { it(movie) }
            }
        }
        Log.i("_MOVIE", movie.toString())
    }

    // var of click listener
    private var onItemClickListener: ((Movie) -> Unit)? = null

    private fun setOnItemClickListener(listener: (Movie) -> Unit) {
        onItemClickListener = listener
    }
}