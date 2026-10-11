package com.example.capstone.ui.detail

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.capstone.R
import com.example.capstone.databinding.FragmentDetailBinding
import com.example.capstone.model.MovieModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        val movie = arguments?.let {
            BundleCompat.getParcelable(it, EXTRA_MOVIE, MovieModel::class.java)
        }
        if (movie == null) {
            findNavController().navigateUp()
            return
        }

        setLightStatusBar(false)
        showMovie(movie)
        viewModel.setMovieId(movie.id)

        binding.fabFavorite.setOnClickListener {
            val newState = !viewModel.isFavorite.value
            viewModel.setFavorite(movie, newState)
            Snackbar.make(
                binding.root,
                if (newState) R.string.added_to_favorite else R.string.removed_from_favorite,
                Snackbar.LENGTH_SHORT,
            ).show()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFavorite.collect { isFavorite ->
                    setFavoriteIcon(isFavorite)
                }
            }
        }
    }

    private fun showMovie(movie: MovieModel) {
        with(binding) {
            collapsingToolbar.title = movie.title
            tvTitle.text = movie.title
            tvReleaseDate.text = movie.formattedReleaseDate
            chipRating.text = getString(R.string.rating_format, movie.formattedRating)
            tvOverview.text = movie.overview.ifBlank { getString(R.string.no_overview) }

            Glide.with(this@DetailFragment)
                .load(movie.backdropUrl)
                .placeholder(R.drawable.bg_image_placeholder)
                .error(R.drawable.bg_image_placeholder)
                .into(ivBackdrop)

            Glide.with(this@DetailFragment)
                .load(movie.posterUrl)
                .placeholder(R.drawable.bg_image_placeholder)
                .error(R.drawable.bg_image_placeholder)
                .into(ivPoster)
        }
    }

    private fun setFavoriteIcon(isFavorite: Boolean) {
        binding.fabFavorite.apply {
            setImageResource(if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border)
            contentDescription = getString(
                if (isFavorite) R.string.remove_favorite else R.string.add_favorite
            )
        }
    }

    private fun setLightStatusBar(isLight: Boolean) {
        val window = requireActivity().window
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = isLight
    }

    private fun isNightMode(): Boolean =
        (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
            Configuration.UI_MODE_NIGHT_YES

    override fun onDestroyView() {
        setLightStatusBar(!isNightMode())
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val EXTRA_MOVIE = "movie"
    }
}
