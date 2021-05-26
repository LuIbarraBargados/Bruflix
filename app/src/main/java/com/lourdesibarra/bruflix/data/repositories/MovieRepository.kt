package com.lourdesibarra.bruflix.data.repositories

import com.lourdesibarra.bruflix.data.Result
import com.lourdesibarra.bruflix.data.api.MoviesApi
import com.lourdesibarra.bruflix.data.response.MovieGenresResponse
import com.lourdesibarra.bruflix.data.response.MovieListResponse
import retrofit2.Response

class MovieRepository (private val moviesApi: MoviesApi) {

    suspend fun getTrendingMovies(): Result<MovieListResponse> {
        return moviesApi.getTrendingMovies()
    }

    suspend fun getMovieGenres() : Result<MovieGenresResponse> {
        return moviesApi.getMoviesGenres()
    }

    suspend fun searchMovie(query: String) : Result<MovieListResponse> {
        return moviesApi.searchMovie(query)
    }
}