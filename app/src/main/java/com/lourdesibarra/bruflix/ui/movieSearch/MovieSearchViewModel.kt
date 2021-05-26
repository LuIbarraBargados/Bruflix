package com.lourdesibarra.bruflix.ui.movieSearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lourdesibarra.bruflix.entities.Genre
import com.lourdesibarra.bruflix.entities.Movie
import com.lourdesibarra.bruflix.data.ApiClient
import com.lourdesibarra.bruflix.data.Result
import com.lourdesibarra.bruflix.data.api.MoviesApi
import com.lourdesibarra.bruflix.data.repositories.MovieRepository
import com.lourdesibarra.bruflix.utils.MoviesConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.Exception

class MovieSearchViewModel : ViewModel() {

    val moviesLiveData = MutableLiveData<List<Movie>>()
    val errorLiveData = MutableLiveData<String>()
    private val movieRepository = MovieRepository(MoviesApi())

    fun searchMovie(query: String, genres: List<Genre>?, subscribedMovies: List<Movie>?) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = movieRepository.searchMovie(query)
            withContext(Dispatchers.Main) {
                when (result) {
                    is Result.Success -> {
                        result.model?.let {
                            val movies = MoviesConverter.convertTrendingMoviesResponseToMovieList(
                                it,
                                genres,
                                subscribedMovies
                            )
                            moviesLiveData.postValue(movies)
                        }
                    }
                    is Result.Error -> {
                        errorLiveData.postValue(result.exception.message)
                    }
                }
            }
        }
    }

    fun updateMovieSearchResult(subscribedMovies: List<Movie>) {
        moviesLiveData.value?.let {
            moviesLiveData.postValue(it.onEach { movie ->
                movie.isSubscribed =
                    subscribedMovies.any { subscribedMovie -> subscribedMovie.id == movie.id }
            })
        }
    }

}