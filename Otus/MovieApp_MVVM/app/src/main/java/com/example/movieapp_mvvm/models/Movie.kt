package com.example.movieapp_mvvm.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "movies"
)
data class Movie(
    @PrimaryKey(autoGenerate = true)
    var keyId: Int? = null,

    val id: Int,
    val title: String
)