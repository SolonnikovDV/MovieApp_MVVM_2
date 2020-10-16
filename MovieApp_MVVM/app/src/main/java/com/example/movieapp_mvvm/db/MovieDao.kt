package com.example.movieapp_mvvm.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.movieapp_mvvm.models.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(movie: Movie): Long

    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Movie>>

    @Delete
    suspend fun deleteMovie(movie: Movie)
}