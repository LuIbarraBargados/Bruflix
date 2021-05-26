package com.lourdesibarra.bruflix.ui.movieSearch

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lourdesibarra.bruflix.MovieSearchItemListener
import com.lourdesibarra.bruflix.databinding.ActivityMovieSearchBinding
import com.lourdesibarra.bruflix.entities.Genre
import com.lourdesibarra.bruflix.entities.Movie
import com.lourdesibarra.bruflix.ui.error.ErrorActivity
import com.lourdesibarra.bruflix.ui.movieDetail.MovieDetailActivity
import com.lourdesibarra.bruflix.utils.SharedPreferencesManager

class MovieSearchActivity : AppCompatActivity(), MovieSearchItemListener {

    companion object {
        const val GENRES_EXTRA_KEY = "GENRES"
    }

    private lateinit var binding: ActivityMovieSearchBinding
    lateinit var viewModel: MovieSearchViewModel
    private var genres: List<Genre>? = null
    private var subscribedMovies: List<Movie>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        genres = intent.getParcelableArrayListExtra(GENRES_EXTRA_KEY)
        setupViews()
        initViewModel()
    }

    override fun onResume() {
        super.onResume()
        getSubscribedMovies()
        subscribedMovies?.let {
            viewModel.updateMovieSearchResult(it)
        }
        binding.searchView.requestFocus()
    }

    private fun getSubscribedMovies() {
        subscribedMovies = SharedPreferencesManager.getSubscribedMovies(this)?.movies?.toList()
    }

    private fun setupViews() {
        binding.apply {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let {
                        viewModel.searchMovie(newText, genres, subscribedMovies)
                    }
                    return true
                }

            })
            btnCancel.setOnClickListener {
                finishAfterTransition()
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[MovieSearchViewModel::class.java]
        viewModel.moviesLiveData.observe(this, Observer { movies ->
            setupMovieSearchResults(movies)
        })
        viewModel.errorLiveData.observe(this, Observer {
            goToErrorActivity()
        })
    }

    private fun setupMovieSearchResults(movies: List<Movie>) {
        binding.rvSearch.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        binding.rvSearch.isNestedScrollingEnabled = false
        binding.rvSearch.adapter = MovieSearchAdapter(movies, this)
    }

    override fun onMovieClick(movie: Movie) {
        val i = Intent(this, MovieDetailActivity::class.java)
        i.putExtra(MovieDetailActivity.MOVIE_EXTRA_KEY, movie)
        startActivity(i)
    }

    override fun onMovieSubscribeButtonClick(movie: Movie) {
        if (movie.isSubscribed) {
            movie.isSubscribed = false
            SharedPreferencesManager.deleteSubscribedMovie(this, movie)
        } else {
            movie.isSubscribed = true
            SharedPreferencesManager.saveMovie(this, movie)
        }
    }

    private fun goToErrorActivity() {
        val intent = Intent(this, ErrorActivity::class.java)
        startActivity(intent)
        finish()
    }

}