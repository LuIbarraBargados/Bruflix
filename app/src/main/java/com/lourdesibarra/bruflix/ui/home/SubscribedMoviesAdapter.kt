package com.lourdesibarra.bruflix.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lourdesibarra.bruflix.MovieItemListener
import com.lourdesibarra.bruflix.databinding.ItemSubscribedMoviesBinding
import com.lourdesibarra.bruflix.entities.Movie
import com.lourdesibarra.bruflix.remote.ApiClient

class SubscribedMoviesAdapter(
    private val subscribedMovies: List<Movie>,
    private val listener: MovieItemListener
) : RecyclerView.Adapter<SubscribedMoviesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubscribedMoviesAdapter.ViewHolder {
        return ViewHolder(
            ItemSubscribedMoviesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SubscribedMoviesAdapter.ViewHolder, position: Int) {
        holder.onBind(subscribedMovies[position])
    }

    override fun getItemCount(): Int = subscribedMovies.size

    inner class ViewHolder(private val itemBinding: ItemSubscribedMoviesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun onBind(movie: Movie) {
            itemBinding.apply {
                root.setOnClickListener {
                    listener.onMovieClick(movie)
                }
                Glide
                    .with(itemBinding.ivSubscribedMovie.context)
                    .load(ApiClient.IMAGE_BASE_URL + movie.posterPath)
                    .into(itemBinding.ivSubscribedMovie)
            }
        }
    }
}