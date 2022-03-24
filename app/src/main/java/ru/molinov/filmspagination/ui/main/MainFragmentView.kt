package ru.molinov.filmspagination.ui.main

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.SingleState

@AddToEndSingle
interface MainFragmentView : MvpView {

    fun renderData()

    fun notifyItemsExclude(position: Int, range: IntRange, scroll: Boolean)

    fun removeRange(range: List<Int>)

    fun addRange(range: List<Int>)

    @SingleState
    fun showAlertDialog(message: Int)
}
