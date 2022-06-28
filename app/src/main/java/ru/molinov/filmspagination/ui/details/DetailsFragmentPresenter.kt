package ru.molinov.filmspagination.ui.details

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.molinov.filmspagination.model.Movie

class DetailsFragmentPresenter(
    private val data: Movie?,
    private val router: Router
) : MvpPresenter<DetailsFragmentView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        data?.apply {
            viewState.setTitle(original_title)
            viewState.setPoster(poster_path)
            viewState.setBackDrop(backdrop_path)
            viewState.setName(title)
            viewState.setYear(release_date)
            viewState.setRating(vote_average.toString())
            viewState.setDescription(overview)
            viewState.setActionBar(original_title)
        }
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}
