package ru.molinov.filmspagination.ui.details

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.molinov.filmspagination.model.Cast
import ru.molinov.filmspagination.model.Movie

@AddToEndSingle
interface DetailsFragmentView : MvpView {

    fun showData(movie: Movie)
    fun showCasts(casts: List<Cast>)
    fun showError(error: String)
    fun showAlertDialog(message: Int)
}
