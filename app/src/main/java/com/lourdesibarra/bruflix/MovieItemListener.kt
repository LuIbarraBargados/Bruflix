package com.lourdesibarra.bruflix

import com.lourdesibarra.bruflix.entities.Movie

interface MovieItemListener {
    fun onMovieClick(movie: Movie)
}