package ru.molinov.filmspagination.ui.main

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.SingleState
import ru.molinov.filmspagination.model.Film
import ru.molinov.filmspagination.model.Genre

@AddToEndSingle
interface MainFragmentView : MvpView {

    fun renderAllFilms(films: List<Film>)

    fun renderFilteredFilms(films: List<Film>)

    fun renderGenres(genres: List<Genre>)

    fun notifyItemsExclude(position: Int, range: IntRange, scroll: Boolean)

    fun removeRange(range: List<Int>)

    fun addRange(range: List<Int>)

    @SingleState
    fun showAlertDialog(message: Int)

    fun showError(message: String)
}
