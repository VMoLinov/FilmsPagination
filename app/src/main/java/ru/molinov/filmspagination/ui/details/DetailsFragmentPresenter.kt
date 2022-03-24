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
            viewState.setImage(image_url)
            viewState.setName(name)
            viewState.setYear(year.toString())
            viewState.setRating(rating.toString())
            viewState.setDescription(description)
            viewState.setActionBar(localized_name)
        }
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}
