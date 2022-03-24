package ru.molinov.filmspagination.ui.details

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface DetailsFragmentView : MvpView {

    fun setName(name: String?)

    fun setYear(year: String?)

    fun setRating(rating: String?)

    fun setImage(url: String?)

    fun setDescription(description: String?)

    fun setActionBar(title: String?)
}
