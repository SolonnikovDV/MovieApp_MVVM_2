package com.example.movieapp_mvvm.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
    tableName = "movies"
)
data class Movie(
    @PrimaryKey(autoGenerate = true)
    var keyId: Int? = null,

    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    val id: Int,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("original_language")
    val originalLanguage: String
) : Serializable