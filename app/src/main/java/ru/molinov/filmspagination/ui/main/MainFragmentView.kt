package ru.molinov.filmspagination.ui.main

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import ru.molinov.filmspagination.model.Genre
import ru.molinov.filmspagination.model.Movie

@AddToEndSingle
interface MainFragmentView : MvpView {

    fun renderAllFilms(movies: List<Movie>)

    fun renderFilteredFilms(movies: List<Movie>)

    fun renderGenres(genres: List<Genre>)

    fun notifyItemsExclude(position: Int, range: IntRange, scroll: Boolean)

    fun removeRange(range: List<Int>)

    fun addRange(range: List<Int>)

    @OneExecution
    fun showAlertDialog(message: Int)

    fun showError(message: String)
}
