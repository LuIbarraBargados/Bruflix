package com.lourdesibarra.bruflix.ui.home

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lourdesibarra.bruflix.MovieItemListener
import com.lourdesibarra.bruflix.databinding.ActivityHomeBinding
import com.lourdesibarra.bruflix.entities.Genre
import com.lourdesibarra.bruflix.entities.Movie
import com.lourdesibarra.bruflix.ui.error.ErrorActivity
import com.lourdesibarra.bruflix.ui.movieDetail.MovieDetailActivity
import com.lourdesibarra.bruflix.ui.movieSearch.MovieSearchActivity
import com.lourdesibarra.bruflix.utils.SharedPreferencesManager
import java.util.*

class HomeActivity : AppCompatActivity(), MovieItemListener {

    private var genres: List<Genre>? = null
    private var subscribedMovies: List<Movie>? = null
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        getGenres()
        setupViews()
    }

    private fun setupViews() {
        binding.toolbarHome.ivSearch.setOnClickListener {
            goToSearchActivity(genres)
        }
    }

    override fun onResume() {
        super.onResume()
        setupSubscribedMoviesList()
    }

    private fun setupSubscribedMoviesList() {
        subscribedMovies = SharedPreferencesManager.getSubscribedMovies(this)?.movies?.toList()
        subscribedMovies?.let {
            binding.tvSubscribedMovies.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            binding.rvSubscribedMovies.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            binding.rvSubscribedMovies.layoutManager = LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false
            )
            binding.rvSubscribedMovies.adapter = SubscribedMoviesAdapter(it.toList(), this)
            viewModel.updateTrendingMovies(it)
        }

    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        viewModel.genreLiveData.observe(this) { genres ->
            this.genres = genres
            viewModel.getTrendingMoviesThisWeek(genres, subscribedMovies)
        }
        viewModel.moviesLiveData.observe(this) { movies ->
            setupTrendingMovieList(movies)
        }
        viewModel.errorLiveData.observe(this) {
            goToErrorActivity()
        }
    }

    private fun setupTrendingMovieList(movies: List<Movie>) {
        binding.rvHomeMovies.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        binding.rvHomeMovies.isNestedScrollingEnabled = false
        binding.rvHomeMovies.adapter = HomeMovieAdapter(movies, this)
    }

    private fun getGenres() {
        viewModel.getGenres()
    }

    override fun onMovieClick(movie: Movie) {
        goToMovieDetailActivity(movie)
    }

    private fun goToMovieDetailActivity(movie: Movie) {
        val i = Intent(this, MovieDetailActivity::class.java)
        i.putExtra(MovieDetailActivity.MOVIE_EXTRA_KEY, movie)
        startActivity(i)
    }

    private fun goToSearchActivity(genres: List<Genre>?) {
        val i = Intent(this, MovieSearchActivity::class.java)
        genres?.let {
            i.putExtra(MovieSearchActivity.GENRES_EXTRA_KEY, ArrayList(genres))
        }
        startActivity(
            i, ActivityOptions
                .makeSceneTransitionAnimation(
                    this,
                    binding.toolbarHome.ivSearch,
                    "searchTransition"
                ).toBundle()
        )
    }

    private fun goToErrorActivity() {
        val intent = Intent(this, ErrorActivity::class.java)
        startActivity(intent)
        finish()
    }
}