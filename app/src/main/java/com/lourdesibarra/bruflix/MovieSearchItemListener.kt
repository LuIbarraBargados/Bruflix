package com.lourdesibarra.bruflix

import com.lourdesibarra.bruflix.entities.Movie

interface MovieSearchItemListener {
    fun onMovieClick(movie: Movie)
    fun onMovieSubscribeButtonClick(movie: Movie)
}