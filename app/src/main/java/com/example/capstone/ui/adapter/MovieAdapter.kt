package com.example.capstone.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstone.R
import com.example.capstone.databinding.ItemMovieBinding
import com.example.capstone.model.MovieModel

class MovieAdapter(
    private val onItemClick: (MovieModel) -> Unit,
) : ListAdapter<MovieModel, MovieAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(
        private val binding: ItemMovieBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieModel) {
            with(binding) {
                tvTitle.text = movie.title
                tvReleaseDate.text = movie.formattedReleaseDate
                tvRating.text = movie.formattedRating
                tvOverview.text = movie.overview.ifBlank {
                    root.context.getString(R.string.no_overview)
                }
                Glide.with(root.context)
                    .load(movie.posterUrl)
                    .placeholder(R.drawable.bg_image_placeholder)
                    .error(R.drawable.bg_image_placeholder)
                    .into(ivPoster)
                root.setOnClickListener { onItemClick(movie) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieModel>() {
            override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
                oldItem == newItem
        }
    }
}
