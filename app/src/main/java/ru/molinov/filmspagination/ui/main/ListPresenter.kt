package ru.molinov.filmspagination.ui.main

interface ListPresenter<V> {
    var itemCLickListener: ((V?) -> Unit)?
}
