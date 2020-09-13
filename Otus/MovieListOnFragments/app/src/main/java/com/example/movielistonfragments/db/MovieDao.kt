package com.example.movielistonfragments.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.movielistonfragments.model.Movie

@Dao
interface MovieDao {

    // db update
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(movie: Movie): Long

    @Query("SELECT * FROM movies")
    fun getAllMovies() : LiveData<List<Movie>>

    @Delete
    suspend fun deleteMovie(movie: Movie)
}