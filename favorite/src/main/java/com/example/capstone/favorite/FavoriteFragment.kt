package com.example.capstone.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.favorite.databinding.FragmentFavoriteBinding
import com.example.capstone.favorite.di.injectFavoriteModule
import com.example.capstone.model.MovieModel
import com.example.capstone.ui.adapter.MovieAdapter
import com.example.capstone.ui.detail.DetailFragment
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.capstone.R as AppR

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectFavoriteModule()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieAdapter = MovieAdapter { movie -> navigateToDetail(movie) }
        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = movieAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoriteMovies.filterNotNull().collect { movies ->
                    movieAdapter.submitList(movies)
                    binding.layoutEmpty.isVisible = movies.isEmpty()
                }
            }
        }
    }

    private fun navigateToDetail(movie: MovieModel) {
        findNavController().navigate(
            AppR.id.action_favorite_to_detail,
            bundleOf(DetailFragment.EXTRA_MOVIE to movie),
        )
    }

    override fun onDestroyView() {
        binding.rvFavorite.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
