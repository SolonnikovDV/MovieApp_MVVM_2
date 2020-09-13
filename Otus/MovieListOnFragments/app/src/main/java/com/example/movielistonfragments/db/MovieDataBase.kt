package com.example.movielistonfragments.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movielistonfragments.model.Movie

@Database(
    entities = [Movie::class],
    version = 1
)

abstract class MovieDataBase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

    companion object {
        @Volatile
        private var instance: MovieDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDateBase(context).also { instance = it }
        }

        private fun createDateBase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MovieDataBase::class.java,
                "movie_db.db"
            ).build()
    }
}