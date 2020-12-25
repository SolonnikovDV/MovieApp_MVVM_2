package com.example.movieapp_mvvm.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp_mvvm.R
import com.example.movieapp_mvvm.models.Movie
import com.example.movieapp_mvvm.util.Constants.Companion.IMAGE_PRE_PATH
import kotlinx.android.synthetic.main.movie_list_item.view.*
import java.lang.RuntimeException

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    private val differCallBack = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.movie_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Movie) -> Unit)? = null
    private var onAlarmButtonClickListener: ((Int) -> Unit)? = null

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this)
                .load(IMAGE_PRE_PATH + movie.posterPath)
                .fitCenter()
                .into(ivMovieImage)
            tvTitle.text =
                movie.title + " (" + movie.releaseDate?.split("-", ignoreCase = true)?.get(0) + ")"
            tvOverview.text = movie.overview
            tvPopularity.text = "TMDb rate: " + movie.popularity.toString()
            tvOriginalTitle.text = "(" + movie.originalLanguage + ") " + movie.originalTitle
            tvVoteCount.text = "Votes: " + movie.voteCount.toString()

            // item click
            setOnClickListener {
                onItemClickListener?.let { it(movie) }
                //crash emulation
//                crashEmulation()
            }

            // button click
            val position = holder.adapterPosition
            btnSeeLater.setOnClickListener {
                onAlarmButtonClickListener?.let { it(position) }
                Log.d("Alarm_button", position.toString())
            }

        }
    }

    fun setOnItemClickListener(listener: (Movie) -> Unit) {
        onItemClickListener = listener
    }
    fun setOnAlarmButtonClickListener(listener: (Int) -> Unit) {
        onAlarmButtonClickListener = listener
    }

    //crash emulation
    fun crashEmulation() {
        throw RuntimeException("crash example")
    }
}