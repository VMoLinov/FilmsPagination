package ru.molinov.filmspagination.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.molinov.filmspagination.databinding.FragmentDetailsBinding
import ru.molinov.filmspagination.model.Film
import ru.molinov.filmspagination.navigation.BackButtonListener
import ru.molinov.filmspagination.ui.App
import ru.molinov.filmspagination.ui.MainActivity
import ru.molinov.filmspagination.ui.imageloader.GlideImageLoader

class DetailsFragment : MvpAppCompatFragment(), DetailsFragmentView, BackButtonListener {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val presenter by moxyPresenter {
        DetailsFragmentPresenter(
            arguments?.getParcelable(PARCEL),
            App.instance.router
        )
    }
    private val imageLoader = GlideImageLoader()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val PARCEL = "New_instance"
        fun newInstance(film: Film): DetailsFragment {
            val b = Bundle()
            b.putParcelable(PARCEL, film)
            val f = DetailsFragment()
            f.arguments = b
            return f
        }
    }

    override fun setName(name: String?) {
        binding.name.append(name)
    }

    override fun setYear(year: String?) {
        binding.year.append(year)
    }

    override fun setRating(rating: String?) {
        binding.rating.append(rating)
    }

    override fun setImage(url: String?) {
        imageLoader.loadFilmUrl(url, binding.image)
    }

    override fun setDescription(description: String?) {
        binding.description.text = description
    }

    override fun setActionBar(title: String?) {
        val actionBar = (activity as MainActivity).getToolBar()
        actionBar?.let {
            it.title = title
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun backPressed(): Boolean {
        return presenter.backPressed()
    }
}
