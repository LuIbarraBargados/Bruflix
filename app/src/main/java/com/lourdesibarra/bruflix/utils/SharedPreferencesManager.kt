package com.lourdesibarra.bruflix.utils

import android.content.Context
import com.google.gson.Gson
import com.lourdesibarra.bruflix.entities.Movie
import com.lourdesibarra.bruflix.entities.SubscribedMoviesList

object SharedPreferencesManager {
    private const val PREFERENCES_NAME = "MyPreferences"
    private const val SUBSCRIBED_MOVIES_KEY = "subscribe movies"
    private val converter = Gson()


    private fun getPreferences(context: Context) =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun getSubscribedMovies(context: Context): SubscribedMoviesList? {
        val listInJson = getPreferences(context)
            .getString(SUBSCRIBED_MOVIES_KEY, "")
        return converter.fromJson(listInJson, SubscribedMoviesList::class.java)
    }

    fun saveMovie(context: Context, movie: Movie) {
        var moviesList = getSubscribedMovies(context)
        if (moviesList?.movies == null) {
            moviesList = SubscribedMoviesList(mutableSetOf())
        }
        moviesList.movies.add(movie)
        getPreferences(context)
            .edit()
            .putString(SUBSCRIBED_MOVIES_KEY, converter.toJson(moviesList))
            .apply()
    }

    fun deleteSubscribedMovie(context: Context, movie: Movie) {
        var moviesList = getSubscribedMovies(context)
        if (moviesList == null) {
            moviesList = SubscribedMoviesList(mutableSetOf())
        }

        val subscribedMovie = moviesList.movies.find { it.id == movie.id }
        moviesList.movies.remove(subscribedMovie)
        getPreferences(context)
            .edit()
            .putString(SUBSCRIBED_MOVIES_KEY, converter.toJson(moviesList))
            .apply()
    }
}




