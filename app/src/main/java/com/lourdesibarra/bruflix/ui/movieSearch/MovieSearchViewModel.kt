package com.lourdesibarra.bruflix.ui.movieSearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lourdesibarra.bruflix.entities.Genre
import com.lourdesibarra.bruflix.entities.Movie
import com.lourdesibarra.bruflix.remote.ApiClient
import com.lourdesibarra.bruflix.remote.response.MovieListResponse
import com.lourdesibarra.bruflix.utils.MoviesConverter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieSearchViewModel : ViewModel() {

    val moviesLiveData = MutableLiveData<List<Movie>>()
    val errorLiveData = MutableLiveData<String>()

    fun searchMovie(query: String, genres: List<Genre>?, subscribedMovies: List<Movie>?) {
        val call = ApiClient.movieService.searchMovie(query)
        call.enqueue(object : Callback<MovieListResponse> {
            override fun onResponse(
                call: Call<MovieListResponse>,
                response: Response<MovieListResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val movies = MoviesConverter.convertTrendingMoviesResponseToMovieList(
                            it,
                            genres,
                            subscribedMovies
                        )
                        moviesLiveData.postValue(movies)
                    }
                }
            }

            override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                errorLiveData.postValue(t.message)
            }
        })
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