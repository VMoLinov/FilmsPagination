package ru.molinov.filmspagination.ui.details

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface DetailsFragmentView : MvpView {

    fun setTitle(title: String?)

    fun setName(name: String?)

    fun setYear(year: String?)

    fun setRating(rating: String?)

    fun setPoster(url: String?)

    fun setBackDrop(url: String?)

    fun setDescription(description: String?)

    fun setActionBar(title: String?)
}
