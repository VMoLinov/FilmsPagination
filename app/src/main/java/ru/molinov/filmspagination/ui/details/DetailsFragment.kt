package ru.molinov.filmspagination.ui.details

import android.app.AlertDialog
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.molinov.filmspagination.R
import ru.molinov.filmspagination.databinding.FragmentDetailsBinding
import ru.molinov.filmspagination.model.Cast
import ru.molinov.filmspagination.model.Movie
import ru.molinov.filmspagination.remote.ApiHolder
import ru.molinov.filmspagination.ui.MainActivity
import ru.molinov.filmspagination.ui.imageloader.GlideImageLoader

class DetailsFragment : MvpAppCompatFragment(), DetailsFragmentView {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val presenter by moxyPresenter {
        DetailsFragmentPresenter(ApiHolder, arguments?.getParcelable(PARCEL))
    }
    private val imageLoader = GlideImageLoader()
    private val adapter = CastsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
        val anim =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.fade)
        enterTransition = anim
        exitTransition = anim
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            recyclerArtists.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val PARCEL = "New_instance"
        fun newInstance(movie: Movie): DetailsFragment {
            val b = Bundle()
            b.putParcelable(PARCEL, movie)
            val f = DetailsFragment()
            f.arguments = b
            return f
        }
    }

    override fun showData(movie: Movie) = with(movie) {
        binding.title.text = title
        binding.name.append(original_title)
        binding.year.append(release_date)
        binding.rating.append(vote_average.toString())
        imageLoader.loadFilmPoster(poster_path, binding.poster, null)
        imageLoader.loadFilmBackDrop(backdrop_path, binding.backDrop, null)
        binding.description.text = overview
        setActionBar(title)
    }

    override fun showCasts(casts: List<Cast>) {
        adapter.submitList(casts)
    }

    override fun showAlertDialog(message: Int) {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(message))
            .setPositiveButton(getString(R.string.repeat)) { dialogInterface, _ ->
                dialogInterface.dismiss()
                presenter.getCredits()
            }
            .create()
            .show()
    }

    override fun showError(error: String) {
        Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
    }

    private fun setActionBar(title: String?) {
        val actionBar = (activity as MainActivity).getToolBar()
        actionBar?.let {
            it.title = title
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }
}
