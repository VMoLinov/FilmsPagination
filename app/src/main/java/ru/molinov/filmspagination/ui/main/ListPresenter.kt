package ru.molinov.filmspagination.ui.main

interface ListPresenter<T> {
    var itemCLickListener: ((T?) -> Unit)?
}
