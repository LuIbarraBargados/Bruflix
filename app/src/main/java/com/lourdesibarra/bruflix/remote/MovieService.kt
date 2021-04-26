package com.lourdesibarra.bruflix.remote

import com.lourdesibarra.bruflix.remote.response.MovieGenresResponse
import com.lourdesibarra.bruflix.remote.response.MovieListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("trending/movie/week")
    fun getTrendingMoviesThisWeek(): Call<MovieListResponse>

    @GET("genre/movie/list")
    fun getMovieGenres(): Call<MovieGenresResponse>

    @GET("search/movie")
    fun searchMovie(@Query("query") query: String): Call<MovieListResponse>
}