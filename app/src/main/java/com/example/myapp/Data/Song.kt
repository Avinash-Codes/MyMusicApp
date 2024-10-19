package com.example.myapp.Data

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val image: String
)

data class SearchResponse(
    val results: List<Song>
)
