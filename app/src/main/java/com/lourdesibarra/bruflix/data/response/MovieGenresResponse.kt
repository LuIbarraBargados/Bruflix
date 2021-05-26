package com.lourdesibarra.bruflix.data.response

data class MovieGenresResponse(
    val genres: List<GenreResponse>
)

data class GenreResponse(
    val id: Int,
    val name: String
)
