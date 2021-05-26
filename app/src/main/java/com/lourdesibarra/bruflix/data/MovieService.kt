package com.lourdesibarra.bruflix.data

import com.lourdesibarra.bruflix.data.response.MovieGenresResponse
import com.lourdesibarra.bruflix.data.response.MovieListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("trending/movie/week")
    suspend fun getTrendingMoviesThisWeek(): Response<MovieListResponse>

    @GET("genre/movie/list")
    suspend fun getMovieGenres(): Response<MovieGenresResponse>

    @GET("search/movie")
    suspend fun searchMovie(@Query("query") query: String): Response<MovieListResponse>
}