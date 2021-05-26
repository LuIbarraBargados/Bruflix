package com.lourdesibarra.bruflix.ui.movieSearch

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lourdesibarra.bruflix.MovieSearchItemListener
import com.lourdesibarra.bruflix.R
import com.lourdesibarra.bruflix.databinding.ItemSearchMovieBinding
import com.lourdesibarra.bruflix.entities.Movie
import com.lourdesibarra.bruflix.data.ApiClient

class MovieSearchAdapter(
    private val listMovies: List<Movie>,
    private val listener: MovieSearchItemListener
) : RecyclerView.Adapter<MovieSearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieSearchAdapter.ViewHolder {
        return ViewHolder(
            ItemSearchMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieSearchAdapter.ViewHolder, position: Int) {
        holder.onBind(listMovies[position])
    }

    override fun getItemCount(): Int = listMovies.size


    inner class ViewHolder(private val itemBinding: ItemSearchMovieBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(movie: Movie) {
            itemBinding.tvTitleMovieSearch.text = movie.title
            itemBinding.tvGenreMovieSearch.text = movie.genre

            itemBinding.root.setOnClickListener {
                listener.onMovieClick(movie)
            }
            setupSubscribedButton(movie)
            itemBinding.btnSubscribeMovieSearch.setOnClickListener {
                listener.onMovieSubscribeButtonClick(movie)
                setupSubscribedButton(movie)
            }
            Glide
                .with(itemBinding.ivMovieSearch.context)
                .load(ApiClient.IMAGE_BASE_URL + movie.posterPath)
                .into(itemBinding.ivMovieSearch)

        }


        private fun setupSubscribedButton(movie: Movie) {
            if (movie.isSubscribed) {
                setButtonAsSubscribed()
            } else {
                setButtonAsUnsubscribed()
            }
        }

        private fun setButtonAsSubscribed() {
            itemBinding.btnSubscribeMovieSearch.apply {
                text = context.getString(R.string.remove)
                setTextColor(Color.BLACK)
                setBackgroundColor(Color.WHITE)
            }

        }

        private fun setButtonAsUnsubscribed() {
            itemBinding.btnSubscribeMovieSearch.apply {
                text = context.getString(R.string.add)
                setTextColor(Color.WHITE)
                setBackgroundColor(Color.TRANSPARENT)
            }

        }
    }
}