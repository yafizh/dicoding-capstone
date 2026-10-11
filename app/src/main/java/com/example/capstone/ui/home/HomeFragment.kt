package com.example.capstone.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.R
import com.example.capstone.core.domain.model.Resource
import com.example.capstone.databinding.FragmentHomeBinding
import com.example.capstone.model.MovieModel
import com.example.capstone.ui.adapter.MovieAdapter
import com.example.capstone.ui.detail.DetailFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieAdapter = MovieAdapter { movie -> navigateToDetail(movie) }
        binding.rvMovies.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = movieAdapter
        }

        setupToolbar()
        setupSearch()
        binding.btnRetry.setOnClickListener { viewModel.retry() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movies.collect { resource ->
                    renderMovies(resource, movieAdapter)
                }
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_setting -> {
                    findNavController().navigate(R.id.action_home_to_setting)
                    true
                }

                else -> false
            }
        }
    }

    private fun setupSearch() {
        binding.edtSearch.doAfterTextChanged { text ->
            viewModel.setSearchQuery(text?.toString().orEmpty())
        }
        binding.edtSearch.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                WindowCompat.getInsetsController(requireActivity().window, textView)
                    .hide(WindowInsetsCompat.Type.ime())
                textView.clearFocus()
                true
            } else {
                false
            }
        }
    }

    private fun renderMovies(resource: Resource<List<MovieModel>>, adapter: MovieAdapter) {
        when (resource) {
            is Resource.Loading -> {
                binding.progressBar.isVisible = true
                binding.layoutState.isVisible = false
            }

            is Resource.Success -> {
                binding.progressBar.isVisible = false
                adapter.submitList(resource.data)
                val isEmpty = resource.data.isEmpty()
                showState(
                    isVisible = isEmpty,
                    iconRes = R.drawable.ic_movie,
                    message = getString(R.string.no_movies_found),
                    showRetry = false,
                )
            }

            is Resource.Error -> {
                binding.progressBar.isVisible = false
                adapter.submitList(emptyList())
                showState(
                    isVisible = true,
                    iconRes = R.drawable.ic_error,
                    message = resource.message,
                    showRetry = true,
                )
            }
        }
    }

    private fun showState(isVisible: Boolean, iconRes: Int, message: String, showRetry: Boolean) {
        binding.layoutState.isVisible = isVisible
        if (!isVisible) return
        binding.ivState.setImageResource(iconRes)
        binding.tvState.text = message
        binding.btnRetry.isVisible = showRetry
    }

    private fun navigateToDetail(movie: MovieModel) {
        findNavController().navigate(
            R.id.action_home_to_detail,
            bundleOf(DetailFragment.EXTRA_MOVIE to movie),
        )
    }

    override fun onDestroyView() {
        binding.rvMovies.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
