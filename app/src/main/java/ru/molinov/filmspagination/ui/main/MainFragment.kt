package ru.molinov.filmspagination.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.molinov.filmspagination.R
import ru.molinov.filmspagination.databinding.FragmentMainBinding
import ru.molinov.filmspagination.model.Film
import ru.molinov.filmspagination.model.Genre
import ru.molinov.filmspagination.navigation.BackButtonListener
import ru.molinov.filmspagination.remote.ApiHolder
import ru.molinov.filmspagination.ui.App
import ru.molinov.filmspagination.ui.MainActivity

class MainFragment : MvpAppCompatFragment(), MainFragmentView, BackButtonListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val presenter by moxyPresenter {
        MainFragmentPresenter(
            ApiHolder,
            App.instance.router
        )
    }
    private val filmsAdapter by lazy { FilmsAdapter(presenter.filmsListPresenter) }
    private val genresAdapter by lazy { GenresAdapter(presenter.genresListPresenter) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            recyclerFilms.adapter = filmsAdapter
            recyclerGenres.adapter = genresAdapter
            recyclerFilms.setOnScrollChangeListener { _, _, _, _, _ ->
                if (!binding.recyclerFilms.canScrollVertically(1)) {
                    presenter.loadData()
                }
            }
        }
    }

    override fun renderAllFilms(films: List<Film>) {
        binding.recyclerFilms.post { filmsAdapter.submitList(films) }
        binding.loading.animationView.visibility = View.GONE
    }

    override fun renderFilteredFilms(films: List<Film>) {
        binding.recyclerFilms.post { filmsAdapter.submitList(films) }
    }

    override fun renderGenres(genres: List<Genre>) {
        binding.recyclerGenres.post { genresAdapter.submitList(genres) }
    }

    override fun notifyItemsExclude(position: Int, range: IntRange, scroll: Boolean) {
        repeat(range.count()) {
            binding.recyclerFilms.post {
                if (it != position) filmsAdapter.notifyItemChanged(it)
                if (scroll) binding.recyclerFilms.scrollToPosition(range.last)
            }
        }
    }

    override fun removeRange(range: List<Int>) {
        binding.recyclerFilms.post {
            filmsAdapter.notifyItemRangeRemoved(range.first(), range[range.lastIndex])
        }
    }

    override fun addRange(range: List<Int>) {
        binding.recyclerFilms.post {
            filmsAdapter.notifyItemRangeInserted(range.first(), range[range.lastIndex])
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
                presenter.loadData()
            }
            .create()
            .show()
    }

    override fun backPressed(): Boolean {
        return presenter.backPressed()
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
