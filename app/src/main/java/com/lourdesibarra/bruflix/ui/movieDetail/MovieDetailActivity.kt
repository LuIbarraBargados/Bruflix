package com.lourdesibarra.bruflix.ui.movieDetail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toDrawable
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lourdesibarra.bruflix.R
import com.lourdesibarra.bruflix.databinding.ActivityMovieDetailBinding
import com.lourdesibarra.bruflix.entities.Movie
import com.lourdesibarra.bruflix.data.ApiClient
import com.lourdesibarra.bruflix.ui.error.ErrorActivity
import com.lourdesibarra.bruflix.utils.SharedPreferencesManager

class MovieDetailActivity : AppCompatActivity() {

    companion object {
        const val MOVIE_EXTRA_KEY = "movie_extra"
    }

    private lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading()
        val movie = intent.extras?.getParcelable<Movie>(MOVIE_EXTRA_KEY)
        showMovieDetail(movie)
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun showMovieDetail(movie: Movie?) {
        movie?.let {
            binding.apply {
                tvTitleDetail.text = movie.title
                tvYearDetail.text = movie.releaseDate?.split("-")?.first()
                tvDescriptionDetail.text = movie.overview
                Glide
                    .with(ivMovieDetail.context)
                    .asBitmap()
                    .load(ApiClient.IMAGE_BASE_URL + movie.posterPath)
                    .listener(object : RequestListener<Bitmap> {

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            goToErrorActivity()
                            return true
                        }

                        override fun onResourceReady(
                            resource: Bitmap?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            resource?.let { bitmap ->
                                val palette = Palette.from(bitmap).generate()
                                val colorInt =
                                    palette.darkVibrantSwatch?.rgb ?: getColor(R.color.dark_gray)
                                val drawable = resource.toDrawable(resources).apply {

                                    colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
                                        setSaturation(0F)
                                    })
                                }
                                ivBackground.setImageDrawable(drawable)
                                ivBackground.foreground =
                                    ColorUtils.setAlphaComponent(colorInt, 204).toDrawable()
                                setupViews(colorInt, movie)

                            }
                            return false
                        }
                    })
                    .into(ivMovieDetail)
            }
        }
    }

    private fun setupViews(@ColorInt colorInt: Int, movie: Movie?) {
        movie?.let {
            setupSubscribedButton(movie.isSubscribed, colorInt)
            binding.btnSubscribe.setOnClickListener {
                movie.isSubscribed = !movie.isSubscribed
                setupSubscribedButton(movie.isSubscribed, colorInt)
                if (movie.isSubscribed) {
                    SharedPreferencesManager.saveMovie(this, movie)
                } else {
                    SharedPreferencesManager.deleteSubscribedMovie(this, movie)
                }
            }
        }
        hideLoading()
    }

    private fun setupSubscribedButton(isSubscribed: Boolean, @ColorInt colorInt: Int) {
        if (isSubscribed) {
            setButtonAsSubscribed(colorInt)
        } else {
            setButtonAsUnsubscribed()
        }
    }

    private fun setButtonAsSubscribed(@ColorInt colorInt: Int) {
        binding.apply {
            btnSubscribe.text = getString(R.string.subscribed)
            btnSubscribe.setTextColor(colorInt)
            btnSubscribe.setBackgroundColor(Color.WHITE)
        }
    }

    private fun setButtonAsUnsubscribed() {
        binding.apply {
            btnSubscribe.text = getString(R.string.unsubscribed)
            btnSubscribe.setTextColor(Color.WHITE)
            btnSubscribe.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun showLoading() {
        binding.loading.loadingLayout.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loading.loadingLayout.visibility = View.GONE
    }

    private fun goToErrorActivity() {
        val intent = Intent(this, ErrorActivity::class.java)
        startActivity(intent)
        finish()
    }
}