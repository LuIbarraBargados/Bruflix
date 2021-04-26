package com.lourdesibarra.bruflix.ui.home

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lourdesibarra.bruflix.MovieItemListener
import com.lourdesibarra.bruflix.databinding.ItemHomeMoviesBinding
import com.lourdesibarra.bruflix.entities.Movie
import com.lourdesibarra.bruflix.remote.ApiClient

class HomeMovieAdapter(
    private val listMovies: List<Movie>,
    private val listener: MovieItemListener
) : RecyclerView.Adapter<HomeMovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHomeMoviesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(listMovies[position])
    }

    override fun getItemCount(): Int = listMovies.size

    inner class ViewHolder(private val itemBinding: ItemHomeMoviesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(movie: Movie) {
            itemBinding.tvTitleMovie.text = movie.title
            itemBinding.tvGenreMovie.text = movie.genre

            itemView.setOnClickListener {
                listener.onMovieClick(movie)
            }

            Glide
                .with(itemBinding.ivMovie.context)
                .load(ApiClient.IMAGE_BASE_URL + movie.backdropPath)
                .into(itemBinding.ivMovie)
            itemBinding.ivMovie.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
                setSaturation(0F)
            })

        }
    }

}