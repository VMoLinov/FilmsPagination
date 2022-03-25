package ru.molinov.filmspagination.ui.details

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.molinov.filmspagination.model.Film

class DetailsFragmentPresenter(
    private val data: Film?,
    private val router: Router
) : MvpPresenter<DetailsFragmentView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        data?.apply {
            viewState.setImage(poster_path)
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
