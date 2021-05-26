package com.lourdesibarra.bruflix.utils

import com.lourdesibarra.bruflix.entities.Genre
import com.lourdesibarra.bruflix.entities.Movie
import com.lourdesibarra.bruflix.data.response.MovieGenresResponse
import com.lourdesibarra.bruflix.data.response.MovieListResponse

object MoviesConverter {
    fun convertTrendingMoviesResponseToMovieList(
        trendingMoviesResponse: MovieListResponse,
        genres: List<Genre>?,
        subscribedMovies: List<Movie>?
    ): List<Movie> {
        return trendingMoviesResponse.results.map { movieResponse ->
            Movie(
                id = movieResponse.id,
                title = movieResponse.title,
                genre = genres?.find { it.id == movieResponse.genres.firstOrNull() }?.name,
                backdropPath = movieResponse.backdropPath,
                posterPath = movieResponse.poster,
                overview = movieResponse.overview,
                releaseDate = movieResponse.releaseDate,
                isSubscribed = subscribedMovies?.any { it.id == movieResponse.id } ?: false
            )
        }
    }

    fun convertMovieGenresResponseToGenreList(movieGenresResponse: MovieGenresResponse): List<Genre> {
        return movieGenresResponse.genres.map { genreResponse ->
            Genre(
                id = genreResponse.id,
                name = genreResponse.name
            )
        }
    }
}