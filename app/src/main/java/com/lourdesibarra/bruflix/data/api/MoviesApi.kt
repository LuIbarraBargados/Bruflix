package com.lourdesibarra.bruflix.data.api

import com.lourdesibarra.bruflix.data.ApiClient
import com.lourdesibarra.bruflix.data.Result
import com.lourdesibarra.bruflix.data.response.MovieGenresResponse
import com.lourdesibarra.bruflix.data.response.MovieListResponse

class MoviesApi() {

    suspend fun getTrendingMovies(): Result<MovieListResponse> {
        return try {
            //puedo pasarlo a un handler? si tengo metodos distintos en el movieService
            val response = ApiClient.movieService.getTrendingMoviesThisWeek()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.Success(body)
            } else {
                throw Exception("Response not successful")
            }

        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }

    suspend fun getMoviesGenres(): Result<MovieGenresResponse> {
        return try {
            val response = ApiClient.movieService.getMovieGenres()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.Success(body)
            } else {
                throw Exception("Response not successful")
            }

        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }

    suspend fun searchMovie(query: String): Result<MovieListResponse> {
        return try {
            val response = ApiClient.movieService.searchMovie(query)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.Success(body)
            } else {
                throw Exception("Response not successful")
            }

        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
}
