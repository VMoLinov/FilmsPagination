package ru.molinov.filmspagination.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnAttach
import androidx.core.view.doOnPreDraw
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.molinov.filmspagination.R
import ru.molinov.filmspagination.databinding.FragmentMainBinding
import ru.molinov.filmspagination.model.Genre
import ru.molinov.filmspagination.model.Movie
import ru.molinov.filmspagination.remote.ApiHolder
import ru.molinov.filmspagination.ui.MainActivity

class MainFragment : MvpAppCompatFragment(), MainFragmentView {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val presenter by moxyPresenter {
        MainFragmentPresenter(ApiHolder)
    }
    private val moviesAdapter by lazy {
        MoviesAdapter(
            presenter.moviesListPresenter,
            parentFragmentManager
        )
    }
    private val genresAdapter by lazy { GenresAdapter(presenter.genresListPresenter) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val anim =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.fade)
        enterTransition = anim
        exitTransition = anim
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        with(binding) {
            recyclerFilms.adapter = moviesAdapter
            recyclerGenres.adapter = genresAdapter
            recyclerFilms.doOnPreDraw {
                startPostponedEnterTransition()
            }
            recyclerFilms.setOnScrollChangeListener { _, _, _, _, _ ->
                if (!binding.recyclerFilms.canScrollVertically(1)) {
                    presenter.loadData()
                }
            }
        }
    }

    override fun renderAllFilms(movies: List<Movie>) {
        binding.recyclerFilms.post { moviesAdapter.submitList(movies) }
        binding.loading.animationView.visibility = View.GONE
    }

    override fun renderFilteredFilms(movies: List<Movie>) {
        binding.recyclerFilms.post { moviesAdapter.submitList(movies) }
    }

    override fun renderGenres(genres: List<Genre>) {
        binding.recyclerGenres.post { genresAdapter.submitList(genres) }
    }

    override fun notifyItemsExclude(position: Int, range: IntRange, scroll: Boolean) {
        repeat(range.count()) {
            binding.recyclerFilms.post {
                if (it != position) moviesAdapter.notifyItemChanged(it)
                if (scroll) binding.recyclerFilms.scrollToPosition(range.last)
            }
        }
    }

    override fun removeRange(range: List<Int>) {
        binding.recyclerFilms.post {
            moviesAdapter.notifyItemRangeRemoved(range.first(), range[range.lastIndex])
        }
    }

    override fun addRange(range: List<Int>) {
        binding.recyclerFilms.post {
            moviesAdapter.notifyItemRangeInserted(range.first(), range[range.lastIndex])
        }
    }

    override fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showAlertDialog(message: Int) {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(message))
            .setPositiveButton(getString(R.string.repeat)) { dialogInterface, _ ->
                dialogInterface.dismiss()
                when (message) {
                    R.string.callback_failure -> presenter.loadData()
                    R.string.callback_genres_failure -> presenter.loadGenres()
                }
            }
            .create()
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val actionBar = (activity as MainActivity).getToolBar()
        actionBar?.let {
            it.title = getString(R.string.main_fragment)
            it.setDisplayHomeAsUpEnabled(false)
        }
    }
}
