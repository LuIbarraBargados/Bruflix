package com.lourdesibarra.bruflix.ui.home

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

class HomeViewModel : ViewModel() {

    val moviesLiveData = MutableLiveData<List<Movie>>()
    val genreLiveData = MutableLiveData<List<Genre>>()
    val errorLiveData = MutableLiveData<String>()
    private val movieRepository = MovieRepository(MoviesApi())


    fun getTrendingMoviesThisWeek(genres: List<Genre>, subscribedMovies: List<Movie>?) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = movieRepository.getTrendingMovies()
            withContext(Dispatchers.Main) {
                when (result) {
                    is Result.Success -> {
                        // puedo mandarlo a otra capa?
                        val movies = MoviesConverter.convertTrendingMoviesResponseToMovieList(
                            result.model,
                            genres,
                            subscribedMovies
                        )
                        //
                        moviesLiveData.postValue(movies)
                    }
                    is Result.Error -> {
                        errorLiveData.postValue(result.exception.message)
                    }
                }
            }
        }
    }

    fun getGenres() = viewModelScope.launch(Dispatchers.IO) {
        val result = movieRepository.getMovieGenres()
        withContext(Dispatchers.Main) {
            when (result) {
                is Result.Success -> {
                    result.model?.let {
                        val genres = MoviesConverter.convertMovieGenresResponseToGenreList(it)
                        genreLiveData.postValue(genres)
                    }
                }
                is Result.Error -> {
                    errorLiveData.postValue(result.exception.message)
                }
            }
        }
    }


    fun updateTrendingMovies(subscribedMovies: List<Movie>) {
        moviesLiveData.value?.let {
            moviesLiveData.postValue(it.onEach { movie ->
                movie.isSubscribed =
                    subscribedMovies.any { subscribedMovie -> subscribedMovie.id == movie.id }
            })
        }
    }
}